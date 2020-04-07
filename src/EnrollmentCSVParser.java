import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class EnrollmentCSVParser {

  public static void main(String[] args) throws IOException {
    String filepath = args[0];

    BufferedReader csvReader = new BufferedReader(new FileReader(filepath));

    HashMap<String, User> users = new HashMap<>();

    try {
      String row;

      while ((row = csvReader.readLine()) != null) {
        String[] fields = row.split(",");

        ArrayList<String> firstAndLastName = new ArrayList<>(Arrays.asList(fields[1].split(" ")));

        String firstName = firstAndLastName.get(0);
        firstAndLastName.remove(0);

        String lastname = firstAndLastName.stream().reduce("", (acc, curr) -> {
          return acc + " " + curr;
        });

        User user = new User(
            fields[0],
            firstName,
            lastname.trim(),
            Integer.valueOf(fields[2]),
            fields[3]
        );

        if(users.get(user.getId()) == null || users.get(user.getId()).getVersion() < user.getVersion()) {
          users.put(user.getId(), user);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    csvReader.close();

    HashMap<String, ArrayList<User>> map = new HashMap<>();

    users.values().forEach(user -> {
      String insuranceCompany = Optional.ofNullable(user.getInsuranceCompany())
        .orElse("No Insurance Company");

      Optional.ofNullable(map.get(insuranceCompany))
        .ifPresentOrElse(
          list -> {
            list.add(user);
          },
          () -> {
            ArrayList<User> list = new ArrayList<>();
            list.add(user);
            map.put(insuranceCompany, list);
          }
        );

    });

    for (String insuranceCompany: map.keySet()) {
      BufferedWriter csvWriter = new BufferedWriter(new FileWriter(filepath.replaceAll(".csv", "_" + insuranceCompany + ".csv")));

      ArrayList<User> sortedUsers = map.get(insuranceCompany).stream()
          .sorted(new Comparator<>() {
            @Override
            public int compare(User o1, User o2) {
              return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
            }
          })
          .sorted(new Comparator<>() {
            @Override
            public int compare(User o1, User o2) {
              return o1.getLastName().compareToIgnoreCase(o2.getLastName());
            }
          })
          .collect(Collectors.toCollection(ArrayList::new));

      sortedUsers.forEach(user -> {
        try {
          csvWriter.write(user.getId() + "," + user.getLastName() + "," + user.getFirstName() + "," + user.getVersion() +"\n");
        } catch (IOException e) {
          e.printStackTrace();
        }
      });

      csvWriter.close();
    }

  }

}
