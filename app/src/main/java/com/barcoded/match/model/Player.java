package com.barcoded.match.model;

/**
 * This Player enumeration lists the player characters for the Match game in both regular and
 * reverse mode. There is no multiplayer mode planned, so this consists of Player Character (PC),
 * Non-Player Character (NPC) and neither (NOBODY).
 *
 * @see Board;
 * @see Match;
 * @version 1.0
 * @author Kai Dauberschmidt
 * @since {2021-01-27}
 */
public enum Player {

    /** The (human) player character. */
    PC,

    /** The machine player, also known as non player character. */
    NPC,

    /** This is neither PC nor NPC. */
    NOBODY
}
