package AnimalRegistryProgram;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

public class Animal {
    private String name;
    private LocalDate birthDate;
    private List<String> commands = new ArrayList<>();

    public Animal(String name, LocalDate birthday) {
        this.name = name;
        this.birthDate = birthday;
    }

    public String getName() {
        return name;
    }


    public LocalDate getBirthDate() {
        return birthDate;
    }


    public List<String> getCommands() {
        return commands;
    }


    public void addCommand(String newCommand) {
        commands.add(newCommand);
    }
}
