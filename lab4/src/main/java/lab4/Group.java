package lab4;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Group {
    private final String realSender;
    private final String fakeSender;
    private final ArrayList<String> victims;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", Pattern.CASE_INSENSITIVE);

    /**
     * @param groupSize Must be over 2. Size of the group of victims including the fake sender
     * @throws RuntimeException Group size bellow 3 or config files not found or invalid
     */
    public Group(int groupSize) {
        if (groupSize < 3) {
            throw new RuntimeException("Group size must be over 2");
        }

        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("./lab4/src/config/mailList.json")) {
            //Read JSON file
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            JSONArray mailList = (JSONArray) jsonObject.get("mailList");

            //Iterate over mailList array
            ArrayList<String> group = getRandomMailAddress(mailList, groupSize + 1);
            realSender = group.get(0);
            fakeSender = group.get(1);
            group.remove(1);
            group.remove(0);
            victims = new ArrayList<>();
            victims.addAll(group);

        } catch (IOException | ParseException | RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("config/mailList.json files not found or invalid");
        }
    }

    public String getRealSender() {
        return realSender;
    }

    public String getFakeSender() {
        return fakeSender;
    }

    public String[] getVictims() {
        return victims.toArray(new String[0]);
    }

    /**
     * Get distinct number of random objects from a JSONArray. Inspired by
     * <a href="https://stackoverflow.com/q/40164555/8924032">Stackoverflow comment</a>
     *
     * @param jsonArray array to parse
     * @param groupSize number of distinct objects to retrieve
     * @throws RuntimeException Not enough email address are found in jsonArray
     * @return array of indexes
     */
    private ArrayList<String> getRandomMailAddress(JSONArray jsonArray, int groupSize){
        Random random = new Random();
        Set<Integer> generated = new LinkedHashSet<>();
        Set<Integer> indexesTested = new LinkedHashSet<>();

        while (generated.size() < groupSize) {
            int rndIndex = random.nextInt(jsonArray.size());

            if(indexesTested.add(rndIndex)) { // If the index has not been tested already
                if (VALID_EMAIL_ADDRESS_REGEX.matcher((String) jsonArray.get(rndIndex)).matches()) {
                    generated.add(rndIndex); // Cannot add twice the same Integer
                } else {
                    System.err.println("Invalid email address found: " + jsonArray.get(rndIndex));
                }
            }
            // Prevent infinite loop in case of a bad JSONArray
            if (indexesTested.size() == jsonArray.size() && generated.size() != groupSize)
                throw new RuntimeException("Not enough email address found in jsonArray");
        }
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i : generated) {
            arrayList.add((String) jsonArray.get(i));
        }
        return arrayList;
    }
}
