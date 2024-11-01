package Entities;

import DataStructures.LinkedList.LinkedList;
import DataStructures.Nodes.Pairs;

public class VoteOptionManager {
    private final LinkedList<Pairs<Integer, VoteOption>> availableOptions;
    private final int pollID;

    public VoteOptionManager(int pollID){
        availableOptions = new LinkedList<>();
        this.pollID = pollID;
    }

    public void addOption(int localVoteID, VoteOption option){
        availableOptions.append(new Pairs<>(localVoteID, option));
    }

    public LinkedList<Pairs<Integer, VoteOption>> getAvailableOptions() {
        return availableOptions;
    }

    public int getPollID() {
        return pollID;
    }
}
