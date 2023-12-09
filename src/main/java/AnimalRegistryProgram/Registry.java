package AnimalRegistryProgram;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Registry {
    private List<Pet> domesticPets = new ArrayList<>();
    private List<PackAnimal> packAnimals = new ArrayList<>();


    public void navigateMenu() {
        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
            System.out.println("Меню навигации:");
            System.out.println("1. Просмотреть список всех животных");
            System.out.println("2. Просмотреть список домашних животных");
            System.out.println("3. Просмотреть список вьючных животных");
            System.out.println("4. Поиск животного по имени");
            System.out.println("5. Поиск животного по типу (кошка, собака, хомяк, лошадь, верблюд, осел)");
            System.out.println("6. Добавить новое животное");
            System.out.println("7. Научить животное новой команде");
            System.out.println("8. Посмотреть команды, которые умеет выполнять животное");
            System.out.println("0. Выход");

            System.out.print("Введите номер выбранной опции: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> listAllAnimals();
                case 2 -> listDomesticPets();
                case 3 -> listPackAnimals();
                case 4 -> searchByName();
                case 5 -> searchByType();
                case 6 -> addNewAnimal();
                case 7 -> teachCommand();
                case 8 -> listCommands();
                case 0 -> System.out.println("Выход из меню навигации");
                default -> System.out.println("Неверная опция. Пожалуйста, выберите существующий номер.");
            }
        } while (choice != 0);
    }

    private void listAllAnimals() {
        System.out.println("Список всех животных:");
        for (Pet pet : domesticPets) {
            System.out.println("Имя: " + pet.getName() + ", Тип: " + pet.getClass().getSimpleName());
        }
        for (PackAnimal packAnimal : packAnimals) {
            System.out.println("Имя: " + packAnimal.getName() + ", Тип: " + packAnimal.getClass().getSimpleName());
        }
    }



    private void addNewAnimal() {
        try (Counter counter = new Counter()) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Введите имя нового животного:");
            String name = scanner.nextLine();

            System.out.println("Введите дату рождения нового животного в формате YYYY, MM , DD :");
            System.out.print("Год: ");
            int year = scanner.nextInt();
            System.out.print("Месяц: ");
            int month = scanner.nextInt();
            System.out.print("День: ");
            int day = scanner.nextInt();

            LocalDate birthDate = LocalDate.of(year, month, day);

            System.out.println("Выберите тип нового животного (1 - Кошка, 2 - Собака, 3 - Хомяк, 4 - Лошадь, 5 - Верблюд, 6 - Осел):");
            int animalType = scanner.nextInt();

            // Создаем новое животное
            Animal newAnimal = createRegistryAnimal(name, birthDate, animalType);
            counter.add();

            // Добавляем новое животное в соответствующий список
            if (newAnimal instanceof Pet) {
                domesticPets.add((Pet) newAnimal);
            } else if (newAnimal instanceof PackAnimal) {
                packAnimals.add((PackAnimal) newAnimal);
            }

            System.out.println("Добавлено новое животное:");
            System.out.println("Имя: " + newAnimal.getName());
            System.out.println("Дата рождения: " + newAnimal.getBirthDate());
            System.out.println("Тип: " + newAnimal.getClass().getSimpleName());
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
    }



  public static Animal createRegistryAnimal(String name, LocalDate birthDate, int animalType) {
      return switch (animalType) {
          case 1 -> new Cat(name, birthDate);
          case 2 -> new Dog(name, birthDate);
          case 3 -> new Hamster(name, birthDate);
          case 4 -> new Horse(name, birthDate);
          case 5 -> new Camel(name, birthDate);
          case 6 -> new Donkey(name, birthDate);
          default -> throw new IllegalArgumentException("Неверный тип животного");
      };
    }


    private Pet findDomesticPetByName(String name) {
        for (Pet pet : domesticPets) {
            if (pet.getName().equalsIgnoreCase(name)) {
                return pet;
            }
        }
        return null;
    }

    private PackAnimal findPackAnimalByName(String name) {
        for (PackAnimal packAnimal : packAnimals) {
            if (packAnimal.getName().equalsIgnoreCase(name)) {
                return packAnimal;
            }
        }
        return null;
    }

    private void searchByName() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите имя для поиска:");
        String nameToSearch = scanner.nextLine();

        Pet foundDomesticPet = findDomesticPetByName(nameToSearch);
        PackAnimal foundPackAnimal = findPackAnimalByName(nameToSearch);

        if (foundDomesticPet != null) {
            System.out.println("Найдено домашнее животное:");
            System.out.println("Имя: " + foundDomesticPet.getName() + ", Тип: " + foundDomesticPet.getClass().getSimpleName());
        } else if (foundPackAnimal != null) {
            System.out.println("Найдено вьючное животное:");
            System.out.println("Имя: " + foundPackAnimal.getName() + ", Тип: " + foundPackAnimal.getClass().getSimpleName());
        } else {
            System.out.println("Животное с именем '" + nameToSearch + "' не найдено.");
        }
    }

    private void searchByType() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите тип для поиска (кошка, собака, хомяк, лошадь, верблюд, осел):");
        String typeToSearch = scanner.nextLine().toLowerCase();

        List<Pet> matchingDomesticPets = new ArrayList<>();
        List<PackAnimal> matchingPackAnimals = new ArrayList<>();

        for (Pet pet : domesticPets) {
            if (pet.getClass().getSimpleName().toLowerCase().equals(typeToSearch)) {
                matchingDomesticPets.add(pet);
            }
        }

        for (PackAnimal packAnimal : packAnimals) {
            if (packAnimal.getClass().getSimpleName().toLowerCase().equals(typeToSearch)) {
                matchingPackAnimals.add(packAnimal);
            }
        }

        if (!matchingDomesticPets.isEmpty()) {
            System.out.println("Найдены домашние животные по типу '" + typeToSearch + "':");
            for (Pet pet : matchingDomesticPets) {
                System.out.println("Имя: " + pet.getName() + ", Тип: " + pet.getClass().getSimpleName());
            }
        }

        if (!matchingPackAnimals.isEmpty()) {
            System.out.println("Найдены вьючные животные по типу '" + typeToSearch + "':");
            for (PackAnimal packAnimal : matchingPackAnimals) {
                System.out.println("Имя: " + packAnimal.getName() + ", Тип: " + packAnimal.getClass().getSimpleName());
            }
        }

        if (matchingDomesticPets.isEmpty() && matchingPackAnimals.isEmpty()) {
            System.out.println("Животные с типом '" + typeToSearch + "' не найдены.");
        }
    }

    private void teachCommand() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите имя животного, которому хотите добавить новую команду:");
        String name = scanner.nextLine();

        Pet domesticPet = findDomesticPetByName(name);
        PackAnimal packAnimal = findPackAnimalByName(name);

        if (domesticPet != null) {
            System.out.println("Введите новую команду для домашнего животного " + domesticPet.getName() + ":");
            String newCommand = scanner.nextLine();
            domesticPet.addCommand(newCommand);
            System.out.println("Команда добавлена.");
        } else if (packAnimal != null) {
            System.out.println("Введите новую команду для вьючного животного " + packAnimal.getName() + ":");
            String newCommand = scanner.nextLine();
            packAnimal.addCommand(newCommand);
            System.out.println("Команда добавлена.");
        } else {
            System.out.println("Животное с именем '" + name + "' не найдено.");
        }
    }

    private void listCommands() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите имя животного, чтобы увидеть его команды:");
        String name = scanner.nextLine();

        Pet domesticPet = findDomesticPetByName(name);
        PackAnimal packAnimal = findPackAnimalByName(name);

        if (domesticPet != null) {
            System.out.println("Команды для домашнего животного " + domesticPet.getName() + ":");
            List<String> commands = domesticPet.getCommands();
            for (String command : commands) {
                System.out.println(command);
            }
        } else if (packAnimal != null) {
            System.out.println("Команды для вьючного животного " + packAnimal.getName() + ":");
            List<String> commands = packAnimal.getCommands();
            for (String command : commands) {
                System.out.println(command);
            }
        } else {
            System.out.println("Животное с именем '" + name + "' не найдено.");
        }
    }


    public void listDomesticPets() {
        System.out.println("Список домашних животных:");
        for (Pet pet : domesticPets) {
            System.out.println("Имя: " + pet.getName() + ", Тип: " + pet.getClass().getSimpleName());
        }
    }

    public void listPackAnimals() {
        System.out.println("Список вьючных животных:");
        for (PackAnimal packAnimal : packAnimals) {
            System.out.println("Имя: " + packAnimal.getName() + ", Тип: " + packAnimal.getClass().getSimpleName());
        }
    }
}
