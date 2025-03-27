package Model;

import Constants.ProjectConstants;
import Controller.PlayerController;
import Utils.CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Represents a player in the game.
 * @author Disha Padsala
 */
public class Player {

    /** The name of the player. */
    String d_playerName;

    /** The number of unallocated armies the player has. */
    Integer d_unallocatedArmies;

    /** The list of countries the player currently controls. */
    List<Country> d_currentCountries;

    /** The set of continents the player currently controls. */
    Set<Continent> d_currentContinents;

    /** The list of orders the player has issued. */
    List<Orders> d_orders;

    /** Represents a player's log of actions and events. */
    public String d_playerLog;

    /** Indicates whether the player has more orders to execute. */
    boolean d_moreOrders;

    /** List of cards owned by the player. */
    List<String> d_cardOwnedByPlayer = new ArrayList<>();

    /** List of players with whom this player has negotiated. */
    List<Player> d_negotiatePlayer = new ArrayList<>();

    /** Flag to determine if only one card can be used per turn. */
    boolean d_oneCardPerTurn = false;

    /**
     * Constructor to initialize a new player.
     *
     * @param p_playerName The name of the player.
     */
    public Player(String p_playerName) {
        this.d_playerName = p_playerName;
        this.d_unallocatedArmies = 0;
        this.d_orders = new ArrayList<Orders>();
        this.d_currentCountries = new ArrayList<Country>();
        this.d_currentContinents = new HashSet<>();
        this.d_moreOrders = true;
    }

    /**
     * Gets the name of the player.
     *
     * @return The player's name.
     */
    public String getD_playerName() {
        return d_playerName;
    }

    /**
     * Sets the name of the player.
     *
     * @param p_playerName The player's name.
     */
    public void setD_playerName(String p_playerName) {
        this.d_playerName = p_playerName;
    }

    /**
     * Gets the number of unallocated armies for the player.
     *
     * @return The number of unallocated armies.
     */
    public Integer getD_unallocatedArmies() {
        return d_unallocatedArmies;
    }

    /**
     * Sets the number of unallocated armies for the player.
     *
     * @param p_unallocatedArmies The number of unallocated armies.
     */
    public void setD_unallocatedArmies(Integer p_unallocatedArmies) {
        this.d_unallocatedArmies = p_unallocatedArmies;
    }

    /**
     * Gets the list of countries the player currently controls.
     *
     * @return The list of countries.
     */
    public List<Country> getD_currentCountries() {
        return d_currentCountries;
    }

    /**
     * Sets the list of countries the player currently controls.
     *
     * @param p_currentCountries The list of countries.
     */
    public void setD_currentCountries(List<Country> p_currentCountries) {
        this.d_currentCountries = p_currentCountries;
    }

    /**
     * Gets the set of continents the player currently controls.
     *
     * @return The set of continents.
     */
    public Set<Continent> getD_currentContinents() {
        return d_currentContinents;
    }

    /**
     * Sets the set of continents the player currently controls.
     *
     * @param p_currentContinents The set of continents.
     */
    public void setD_currentContinents(Set<Continent> p_currentContinents) {
        this.d_currentContinents = p_currentContinents;
    }

    /**
     * Gets the list of orders the player has issued.
     *
     * @return The list of orders.
     */
    public List<Orders> getD_orders() {
        return d_orders;
    }

    /**
     * Sets the list of orders the player has issued.
     *
     * @param p_orders The list of orders.
     */
    public void setD_orders(List<Orders> p_orders) {
        this.d_orders = p_orders;
    }

    /**
     * Assigns a continent to the player.
     *
     * @param p_continent The continent to be assigned.
     */
    public void setContinent(Continent p_continent) {
        if(d_currentContinents == null) {
            d_currentContinents = new HashSet<>();
        }
        if(!d_currentContinents.add(p_continent)) {
            System.out.println("Continent : "+p_continent.getD_continentName()+" already assigned to Player : "+d_playerName);
        }
    }

    /**
     * Gets the player's log of actions and events.
     *
     * @return The player's log as a String.
     */
    public String getD_playerLog(){
        return d_playerLog;
    }

    /**
     * Sets the player's log of actions and events and prints the log message.
     * If the message type is "error," it prints to the error stream.
     *
     * @param p_orderExecutionLog The message to be logged.
     * @param p_messageType The type of message ("error" or other).
     */
    public void setD_playerLog(String p_orderExecutionLog, String p_messageType){
        this.d_playerLog = p_orderExecutionLog;
        if (p_messageType.equals("error")){
            System.err.println(p_orderExecutionLog);
        } else {
            System.out.println(p_orderExecutionLog);
        }
    }

    /**
     * Checks if the player has more orders to execute.
     *
     * @return {@code true} if the player has more orders; otherwise, {@code false}.
     */
    public boolean isD_moreOrders(){
        return d_moreOrders;
    }

    /**
     * Sets whether the player has more orders to execute.
     *
     * @param d_moreOrders {@code true} if the player has more orders; otherwise, {@code false}.
     */
    public void setD_moreOrders(boolean d_moreOrders){
        this.d_moreOrders = d_moreOrders;
    }

    /**
     * Gets the list of cards owned by the player.
     *
     * @return A list of card names owned by the player.
     */
    public List<String> getD_cardOwnedByPlayer(){
        return d_cardOwnedByPlayer;
    }

    /**
     * Sets the list of cards owned by the player.
     *
     * @param d_cardOwnedByPlayer The list of card names to assign to the player.
     */
    public void setD_cardOwnedByPlayer(List<String> d_cardOwnedByPlayer){
        this.d_cardOwnedByPlayer = d_cardOwnedByPlayer;
    }

    /**
     * Gets the list of players with whom this player has negotiated.
     *
     * @return A list of players the current player has negotiated with.
     */
    public List<Player> getD_negotiatePlayer(){
        return d_negotiatePlayer;
    }

    /**
     * Sets the list of players with whom this player has negotiated.
     *
     * @param d_negotiatePlayer The list of players to set as negotiated with.
     */
    public void setD_negotiatePlayer(List<Player> d_negotiatePlayer){
        this.d_negotiatePlayer = d_negotiatePlayer;
    }

    /**
     * Checks whether the player is limited to using one card per turn.
     *
     * @return {@code true} if the player can only use one card per turn; otherwise, {@code false}.
     */
    public boolean isD_oneCardPerTurn() {
        return d_oneCardPerTurn;
    }

    /**
     * Sets whether the player is limited to using one card per turn.
     *
     * @param d_oneCardPerTurn {@code true} if the player can only use one card per turn; otherwise, {@code false}.
     */
    public void setD_oneCardPerTurn(boolean d_oneCardPerTurn) {
        this.d_oneCardPerTurn = d_oneCardPerTurn;
    }
    /**
     * Issues an order for the player to deploy armies.
     *
     * @throws IOException If there is an issue with reading the input.
     */
    public void issueOrder() throws IOException {
        BufferedReader l_bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter command to deploy armies on the map for Player: " + d_playerName + " | Armies left: " + d_unallocatedArmies);

        String l_command = l_bufferedReader.readLine().trim();
        CommandHandler l_commandHandler = new CommandHandler(l_command);

        if("deploy".equals(l_commandHandler.getMainCommand())) {
            String[] l_commandParts = l_command.split(" ");

            if(l_commandParts.length == 3) {
                PlayerController l_playerController = new PlayerController();
                l_playerController.createDeployOrder(l_command, this);
            } else {
                System.out.println(ProjectConstants.INVALID_COMMAND);
            }
        }
    }

    /**
     * Retrieves the next order the player has issued.
     *
     * @return The next order, or null if no orders exist.
     */
    public Orders nextOrder() {
        if(d_orders == null || d_orders.isEmpty()) {
            return null;
        }
        Orders l_order = d_orders.get(0);
        d_orders.remove(l_order);
        return l_order;
    }

    /**
     * Returns a string representation of the player, including their name, number of unallocated armies,
     * the number of countries and continents they control, and the number of orders they have issued.
     *
     * @return The string representation of the player.
     */
    public String toString() {
        return "Player [Player Name=" + d_playerName + ", Unallocated Armies=" + d_unallocatedArmies + ", Current Countries=" + d_currentCountries.size() + ", Current Continents=" + d_currentContinents.size() + ", Orders=" + d_orders.size() + "]";
    }

    /**
     * Creates a deploy order based on the given command input.
     *
     * @param p_command The deploy command in the format {@code deploy <CountryName> <NoOfArmies>}.
     */
    public void createDeployOrder(String p_command) {
        if (d_orders == null) {
            d_orders = new ArrayList<>();
        }

        String[] l_commandParts = p_command.split(" ");

        if (l_commandParts.length < 3) {
            this.setD_playerLog("Invalid command format! Correct format: deploy <CountryName> <NoOfArmies>", "error");
            return;
        }

        String l_countryName = l_commandParts[1];
        int l_noOfArmiesToDeploy;

        try {
            l_noOfArmiesToDeploy = Integer.parseInt(l_commandParts[2]);
        } catch (NumberFormatException e) {
            this.setD_playerLog("Invalid number of armies! Please enter a valid integer.", "error");
            return;
        }

        if (!validateCountryBelongstoPlayer(this, l_countryName)) {
            this.setD_playerLog("The country " + l_countryName + " does not belong to this player.", "error");
            return;
        }

        if (!isArmyCountValid(this, l_noOfArmiesToDeploy)) {
            this.setD_playerLog(ProjectConstants.INVALID_NO_OF_ARMIES, "error");
            return;
        }

        //Orders l_order = new Deploy(this, l_countryName, l_noOfArmiesToDeploy);
        //d_orders.add(l_order);

        this.setD_unallocatedArmies(this.getD_unallocatedArmies() - l_noOfArmiesToDeploy);

        this.setD_playerLog(ProjectConstants.ORDER_ADDED, "effect");
    }

    /**
     * Checks if the player has enough unallocated armies to deploy.
     *
     * @param p_player The player attempting to deploy armies.
     * @param p_noOfArmiesToDeploy The number of armies the player wants to deploy.
     * @return {@code true} if the player has enough unallocated armies; otherwise, {@code false}.
     */
    private boolean isArmyCountValid(Player p_player, int p_noOfArmiesToDeploy) {
        return p_player.getD_unallocatedArmies() >= p_noOfArmiesToDeploy;
    }

    /**
     * Validates whether a given country belongs to the specified player.
     *
     * @param p_player The player whose ownership is being checked.
     * @param p_countryName The name of the country to verify.
     * @return {@code true} if the country belongs to the player; otherwise, {@code false}.
     */
    private boolean validateCountryBelongstoPlayer(Player p_player, String p_countryName) {
        for (Country l_eachCountry : p_player.getD_currentCountries()) {
            if (l_eachCountry.getD_countryName().equals(p_countryName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prompts the player to check if they want to give more orders in the next turn.
     * Reads user input from the console and sets the player's order status accordingly.
     */
    public void checkForMoreOrder() {
        try (BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in))) {
            boolean l_validInput = false;

            while (!l_validInput) {
                System.out.println("Do you still want to give orders for player: " + this.getD_playerName() + " in the next turn?");
                System.out.println("Press Y for Yes and N for No");

                String l_check = l_reader.readLine();

                if (l_check.equalsIgnoreCase("Y") || l_check.equalsIgnoreCase("N")) {
                    this.setD_moreOrders(l_check.equalsIgnoreCase("Y"));
                    l_validInput = true;
                } else {
                    System.err.println("Invalid input! Please enter 'Y' or 'N'.");
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading input: " + e.getMessage());
        }
    }

    /**
     * Creates an advance order based on the given command.
     *
     * @param p_inputCommand The advance command in the format {@code advance <SourceCountry> <TargetCountry> <NoOfArmies>}.
     * @param p_currentState The current state of the game, used for validation.
     */
    public void createAdvanceOrder(String p_inputCommand, CurrentState p_currentState) {
        String[] l_commandParts = p_inputCommand.split(" ");

        if (l_commandParts.length != 4) {
            this.setD_playerLog("Invalid command format for Advance order.", "error");
            return;
        }

        String l_sourceCountry = l_commandParts[1];
        String l_targetCountry = l_commandParts[2];
        Integer l_noOfArmies;

        try {
            l_noOfArmies = Integer.parseInt(l_commandParts[3]);
        } catch (NumberFormatException e) {
            this.setD_playerLog("Invalid number of armies specified.", "error");
            return;
        }

        if (!checkCountryPresent(l_sourceCountry, p_currentState) ||
                !checkCountryPresent(l_targetCountry, p_currentState)) {
            return;
        }

        if (l_noOfArmies <= 0) {
            this.setD_playerLog("Number of armies must be greater than 0.", "error");
            return;
        }

        /*if (!validateCountryBelongstoPlayer(this, l_sourceCountry, p_currentState)) {
            this.setD_playerLog("Source country does not belong to the player.", "error");
            return;
        }*/

        if (!checkAdjacentCountry(l_sourceCountry, l_targetCountry, p_currentState)) {
            this.setD_playerLog("Target country is not adjacent to the source country.", "error");
            return;
        }

        //this.d_orders.add(new Advance(l_sourceCountry, l_targetCountry, l_noOfArmies, this));
        this.setD_playerLog("Advance order added successfully for player " + this.getD_playerName(), "log");
    }

    /**
     * Checks whether the target country is adjacent to the source country.
     *
     * @param p_sourceCountry The name of the source country.
     * @param p_targetCountry The name of the target country.
     * @param p_currentState The current state of the game, used to retrieve country details.
     * @return {@code true} if the target country is adjacent to the source country; otherwise, {@code false}.
     */
    private boolean checkAdjacentCountry(String p_sourceCountry, String p_targetCountry, CurrentState p_currentState) {
        Country l_sourceCountry = p_currentState.getD_map().getCountryByName(p_sourceCountry);
        Country l_targetCountry = p_currentState.getD_map().getCountryByName(p_targetCountry);

        if (l_sourceCountry == null || l_targetCountry == null) {
            return false;
        }

        return l_sourceCountry.getD_neighbouringCountriesId().contains(l_targetCountry.getD_countryID());
    }

    /**
     * Checks if a given country exists on the map.
     * If the country is not found, logs an error message.
     *
     * @param p_sourceCountry The name of the country to check.
     * @param p_currentState The current state of the game, used to look up the country.
     * @return {@code true} if the country exists; otherwise, {@code false}.
     */
    private boolean checkCountryPresent(String p_sourceCountry, CurrentState p_currentState) {
        boolean l_exists = p_currentState.getD_map().getCountryByName(p_sourceCountry) != null;

        if (!l_exists) {
            this.setD_playerLog(ProjectConstants.NO_COUNTRIES, "error");
        }

        return l_exists;
    }

    /**
     * Handles the execution of card commands based on the given input.
     * The input command is parsed, and depending on the card type (e.g., bomb, blockade),
     *
     * @param p_inputCommand The command input for the card action (e.g., bomb <country>).
     * @param p_currentState The current state of the game used for order validation.
     */
    public void handleCardCommand(String p_inputCommand, CurrentState p_currentState) {
        String[] l_commandParts = p_inputCommand.split(" ");

        if (!checkCardArguments(l_commandParts)) {
            return;
        }

        String l_commandType = l_commandParts[0].toLowerCase();

        switch (l_commandType) {
            case "bomb":
                Card l_bombOrder = new CardBomb(this, l_commandParts[1]);
                if (l_bombOrder.validOrderCheck(p_currentState)) {
                    this.d_orders.add(l_bombOrder);
                    this.setD_playerLog("Bomb order is added for execution for player " + this.getD_playerName(), "effect");
                    p_currentState.updateLog(getD_playerLog(), "effect");
                }
                break;

            case "blockade":
                Card l_blockadeOrder = new CardBlockade(this, l_commandParts[1]);
                if (l_blockadeOrder.validOrderCheck(p_currentState)) {
                    this.d_orders.add(l_blockadeOrder);
                    this.setD_playerLog("Blockade order is added for execution for player " + this.getD_playerName(), "effect");
                    p_currentState.updateLog(getD_playerLog(), "effect");
                }
                break;

            /*case "airlift":
                try {
                    int l_noOfArmies = Integer.parseInt(l_commandParts[3]);
                    Card l_airliftOrder = new CardAirlift(l_noOfArmies, l_commandParts[1], this, l_commandParts[2], );
                    if (l_airliftOrder.validOrderCheck(p_currentState)) {
                        this.d_orders.add(l_airliftOrder);
                        this.setD_playerLog("Airlift order is added for execution for player " + this.getD_playerName(), "effect");
                        p_currentState.updateLog(getD_playerLog(), "effect");
                    }
                } catch (NumberFormatException e) {
                    this.setD_playerLog("Invalid number of armies for airlift order.", "error");
                }
                break;

            case "negotiate":
                Card l_negotiateOrder = new CardNegotiate(this, l_commandParts[1]);
                if (l_negotiateOrder.validOrderCheck(p_currentState)) {
                    this.d_orders.add(l_negotiateOrder);
                    this.setD_playerLog("Negotiate order is added for execution for player " + this.getD_playerName(), "effect");
                    p_currentState.updateLog(getD_playerLog(), "effect");
                }
                break;*/

            default:
                this.setD_playerLog("Invalid card order type: " + l_commandType, "error");
                break;
        }
    }

    /**
     * Validates the arguments provided for the card command.
     * Ensures that the correct number of arguments is passed based on the card type.
     *
     * @param p_commandParts The parts of the input command (split by spaces).
     * @return {@code true} if the arguments are valid; otherwise, {@code false}.
     */
    private boolean checkCardArguments(String[] p_commandParts) {
        if (p_commandParts.length < 2) {
            this.setD_playerLog("Invalid command format.", "error");
            return false;
        }

        String l_commandType = p_commandParts[0].toLowerCase();

        switch (l_commandType) {
            case "negotiate":
            case "blockade":
            case "bomb":
                if (p_commandParts.length != 2) {
                    this.setD_playerLog("Invalid arguments for " + l_commandType + " order.", "error");
                    return false;
                }
                break;

            case "airlift":
                if (p_commandParts.length != 4) {
                    this.setD_playerLog("Invalid arguments for airlift order. Expected: airlift <source> <target> <armies>", "error");
                    return false;
                }
                break;

            default:
                this.setD_playerLog("Invalid card order type: " + l_commandType, "error");
                return false;
        }
        return true;
    }

    /**
     * Adds a player to the list of players with whom negotiations can be made.
     *
     * @param p_negotiatePlayer The player to add to the negotiation list.
     */
    public void addNegotiatePlayer(Player p_negotiatePlayer){
        this.d_negotiatePlayer.add(p_negotiatePlayer);
    }

    /**
     * Assigns a card to the player. If the player already has a card for the turn or if there are no cards left,
     */
    public void assignCard() {
        if (d_oneCardPerTurn) {
            this.setD_playerLog("Card cannot be assigned to player: " + this.getD_playerName() + " as one card per turn is already assigned.", "error");
            return;
        }

        if (ProjectConstants.ALL_CARDS.isEmpty() || ProjectConstants.NO_OF_CARDS <= 0) {
            this.setD_playerLog("No cards available to assign.", "error");
            return;
        }

        Random l_random = new Random();
        int l_cardIndex = l_random.nextInt(ProjectConstants.NO_OF_CARDS);
        String l_assignedCard = ProjectConstants.ALL_CARDS.get(l_cardIndex);

        this.d_cardOwnedByPlayer.add(l_assignedCard);
        d_oneCardPerTurn = true;

        this.setD_playerLog("Card: " + l_assignedCard + " assigned to player: " + this.getD_playerName(), "effect");
    }

    /**
     * Validates if a player can negotiate with a target country. A player cannot negotiate if they control the target country.
     *
     * @param p_targetCountryName The name of the target country for negotiation.
     * @return {@code true} if negotiation is possible (the player does not control the target country); {@code false} otherwise.
     */
    public boolean negotiationValidation(String p_targetCountryName) {
        boolean l_canAttack = true;
        for(Player l_eachPlayer : d_negotiatePlayer){
            if(l_eachPlayer.getCountryNames().contains(p_targetCountryName)){
                l_canAttack = false;
            }
        }
        return l_canAttack;
    }

    /**
     * Returns a list of country names controlled by the player.
     *
     * @return A list of country names controlled by the player.
     */
    public List<String> getCountryNames() {
        List<String> l_countryNames = new ArrayList<>();
        for(Country l_eachCountry : d_currentCountries){
            l_countryNames.add(l_eachCountry.getD_countryName());
        }
        return l_countryNames;
    }

    /**
     * Removes a specific card from the player's list of owned cards.
     *
     * @param p_cardName The name of the card to be removed.
     */
    public void removeCard(String p_cardName){
        this.d_cardOwnedByPlayer.remove(p_cardName);
    }
}
