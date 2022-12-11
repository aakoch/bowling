package com.adamkoch.bowling;

/**
 * Main entrance to the application now. Pass in a string as an argument to the application.
 */
public class Main {

  public static void main(String[] args) {
    // Input consists of a String representing the game
    if (args.length == 0) {
      // here would go more information on how to use the program
      System.out.println("Please pass in a valid string");
    }
    // It's good practice to validate input to reduce malicious intent.
    // Regex could be a capture group based on the frame. I should come back to this later.
    // I didn't count all the different possibilities, but I think 35 is long enough.
    // Also, by simplifying the code here by not extracting the validation into its own class or method I am severely limiting
    // testing of valid and invalid input.
    else if (args.length == 1) {
      if (args[0].matches("[-\\dX/|]{11,35}")) {
        Game game = Game.fromString(args[0]);
        int score = game.calculateScore();
        if (score == 300) {
          System.out.println("🎳 Perfect game! 300! 🎉");
        }
        else {
          System.out.println("🎳 score = " + score);
        }
      }
      else {
        System.out.println("Invalid input");
        System.exit(1);
      }
    } else {
      System.out.println("Please only one input argument");
      System.exit(1);
    }
  }

  /**
   * Requirements state that the program should take in a string and output an integer score. This method can be used
   * for that purpose.
   *
   * @param frameString The frames as represented as a string
   * @return The score as an integer
   */
  public int oneStep(String frameString) {
    Game game = Game.fromString(frameString);
    return game.calculateScore();
  }

}