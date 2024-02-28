import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
public class GameOfLife {
    private int width;
    private int height;
    private int generations;
    private int speed;
    private boolean[][] board;
    private int specialG;
    private String alive ="\u2B1C";
    private String death ="\u2B1B";
    public GameOfLife(int width, int height, int generations, int speed, String population) {
        this.width = width;
        this.height = height;
        this.generations = generations;
        this.speed = speed;
        this.board = new boolean[height][width];

        if(population.equals("rnd")){
            System.out.println("Random Population");
            population = generatePopulation(width, height);
            initializeBoard(population);
        }else{
        initializeBoard(population);
        }

        if (this.generations == 0){
            startInfinity();
        }
    }

    public GameOfLife(int width, int height, int generations, int speed, String population, int specialG) {
        this.width = width;
        this.height = height;
        this.generations = generations;
        this.speed = speed;
        this.specialG = specialG;
        this.board = new boolean[height][width];

        if(population.equals("rnd")){
            System.out.println("Random Population");
            population = generatePopulation(this.width, this.height);
            initializeBoard(population);
        }else{
            initializeBoard(population);
        }

        if (this.generations == 0){
            startInfinity();
        }
    }
    public void start() {
        for (int gen = 0; gen < this.generations; gen++) {
            System.out.println("Generation: " + (gen + 1) + ":");


            printBoard(alive,death);

            this.board = getNextGeneration();

            try {
                Thread.sleep(this.speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("gen + 1: " + (gen + 1) + ", specialG: " + specialG);
        }
    }


    public void startInfinity() {
        Scanner scanner = new Scanner(System.in);
        boolean keyPressed = false;

        for (int gen = 0; !keyPressed; gen++) {
            System.out.println("Generation: " + (gen + 1) + ":");
            if(gen + 1 >= this.specialG){
                printBoard(death, alive);
            }else{
                printBoard(alive, death);

            }
            this.board = getNextGeneration();
            try {
                if (System.in.available() > 0) {
                    keyPressed = true;
                    scanner.nextLine();
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(this.speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        scanner.close();
    }

    private boolean validatePopulation(String population) {
        if (population == null || population.isEmpty()) {
            throw new IllegalArgumentException("Population cannot be empty");
        }
        String[] rows = population.split("#");
        if (rows.length != this.height) {
            throw new IllegalArgumentException("Invalid Population: incorrect number of rows (" + rows.length + " vs " + height + ")");
        }
        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];
            if (row.length() > this.width) {
                throw new IllegalArgumentException("Invalid Population: row " + (i + 1) + " exceeds width (" + row.length() + " vs " + width + ")");
            }
            for (char c : row.toCharArray()) {
                if (c != '0' && c != '1') {
                    throw new IllegalArgumentException("Invalid Population: row " + (i + 1) + " contains invalid character '" + c + "'");
                }
            }
        }
        return true;
    }
    private void initializeBoard(String population) {
        try {
            validatePopulation(population);
            String[] rows = population.split("#");
            for (int i = 0; i < rows.length; i++) {
                String row = rows[i];
                if (row.length() < this.width) {
                    StringBuilder paddedRow = new StringBuilder(row);
                    while (paddedRow.length() < this.width) {
                        paddedRow.append('0'); // Rellenar con ceros
                    }
                    row = paddedRow.toString();
                } else if (row.length() > this.width) {
                    throw new IllegalArgumentException("Invalid population format: row " + (i + 1) + " exceeds width");
                }
                for (int j = 0; j < this.width; j++) {
                    this.board[i][j] = row.charAt(j) == '1';
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error initializing board: " + e.getMessage());
        }
    }
    private void printBoard(String alive, String death) {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                System.out.print((this.board[i][j] ? alive:death) + " ");
                //System.out.print((board[i][j] ? " + ":" - ") + " ");
            }
            System.out.println(" ");
        }
    }
    private boolean[][] getNextGeneration() {
        boolean[][] newBoard = new boolean[this.height][this.width];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                int aliveNeighbours = countAliveNeighbours(i, j);
                newBoard[i][j] = updateCell(this.board[i][j], aliveNeighbours);
            }
        }
        return newBoard;
    }
    private boolean updateCell(boolean cell, int aliveNeighbours) {
        if (cell) {
            return aliveNeighbours == 2 || aliveNeighbours == 3;
        } else {
            return aliveNeighbours == 3;
        }
    }
    private int countAliveNeighbours(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int neighbourX = x + i;
                int neighbourY = y + j;
                if (neighbourX >= 0 && neighbourX < height && neighbourY >= 0 && neighbourY < width && !(i == 0 && j == 0) && board[neighbourX][neighbourY]) {
                    count++;
                }
            }
        }
        return count;
    }
    private String generatePopulation(int width, int height){
        StringBuilder population = new StringBuilder();
        Random random = new Random();
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                int cell = random.nextInt(2);
                population.append(cell);
            }
            if(i < height - 1){
                population.append("#");
            }
        }
        return population.toString();
    }
}