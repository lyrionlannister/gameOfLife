import java.util.ArrayList;
import java.util.List;

public class Main {
    private static int width = 0;
    private static int height = 0;
    private static int generations = 0;
    private static int speed = 0;
    private static String population = "";
    private static final int[] widthOptions = new int[]{10, 20, 40, 80};
    private static final int[] heightOptions = new int[]{10, 20, 40};
    private static final int[] speedOptions = new int[]{250, 1000};
    private static final String[] argsOptions = new String[]{"w", "h", "g", "s", "p"};
    private static int specialG = 0;

    public static void main(String[] args) {
        if (args.length > 0) {
            String missingArgs = processArgs(args);
            if (!missingArgs.isEmpty()) {
                System.out.println("\nMissing Args: " + missingArgs);
                return;
            }
            try {
                if(generations < 0){
                    throw new IllegalArgumentException("Invalid Generations: g < 0");
                }
                validateRequiredArgs();
                if(specialG != 0){
                    GameOfLife newGame = new GameOfLife(width,height,generations,speed,population, specialG);
                    newGame.start();
                }else {
                GameOfLife game = new GameOfLife(width, height, generations, speed, population);
                game.start();
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("All Args are missing. Use w=width h=height g=generations s=speed p=population");
        }
    }
    private static void validateRequiredArgs() throws IllegalArgumentException {
        if (width == 0) {
            throw new IllegalArgumentException("Width = [No Present]");
        }
        if (height == 0) {
            throw new IllegalArgumentException("Height = [No Present]");
        }
        if (generations < 0) {
            throw new IllegalArgumentException("Generations = [No Present]");
        }
        if (speed == 0) {
            throw new IllegalArgumentException("Speed = [No Present]");
        }
        if (population.isEmpty()) {
            throw new IllegalArgumentException("Population = [No Present]");
        }
        if( specialG != 0 &&  generations < specialG){
            throw new IllegalArgumentException("SpecialG = [No Valid]");
        }
    }
    private static String processArgs(String[] args) {
        List<String> missingArgs = new ArrayList<>();
        for (String arg : args) {
            try {
                String[] argParts = arg.split("=");
                if (argParts.length < 2) {
                    throw new IllegalArgumentException("No value: " + arg);
                }
                String argName = argParts[0].toLowerCase();
                String argValue = argParts[1];
                String response = "";
                switch (argName) {
                    case "w":
                        argName = "Width";
                        width = isValidOption(Integer.parseInt(argValue), widthOptions) ? Integer.parseInt(argValue) : 0;
                        response = width == 0 ? "Invalid Arg" : "";
                        break;
                    case "h":
                        argName = "Height";
                        height = isValidOption(Integer.parseInt(argValue), heightOptions) ? Integer.parseInt(argValue) : 0;
                        response = height == 0 ? "Invalid Arg" : "";
                        break;
                    case "s":
                        argName = "Speed";
                        speed = isValidOption(Integer.parseInt(argValue), speedOptions) ? Integer.parseInt(argValue) : 0;
                        response = speed == 0 ? "Invalid Arg" : "";
                        break;
                    case "g":
                        argName = "Generations";
                        generations = Integer.parseInt(argValue);
                        break;
                    case "p":
                        argName = "Population";
                        population = isValidPopulation(argValue) ? argValue : "";
                        response = population.isEmpty() ? "Invalid Arg" : "";
                        break;
                    case "i":
                        argName = "Special Generation";
                        specialG = Integer.parseInt(argValue);
                        break;
                    default:
                        response = "Unexpected Arg: " + argName;
                        break;
                }
                System.out.printf("%s = [%s]%n", argName, response.isEmpty() ? argValue : response);
                if (!response.isEmpty()) {
                    missingArgs.add(argName);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Format: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Argument: " + e.getMessage());
            }
        }
        return String.join(", ", missingArgs);
    }
    private static boolean isValidOption(int option, int[] validOptions) {
        for (int validValue : validOptions) {
            if (option == validValue) {
                return true;
            }
        }
        return false;
    }
    private static boolean isValidPopulation(String p) {
        return p.matches("[01#]+") || p.equals("rnd");
    }
}