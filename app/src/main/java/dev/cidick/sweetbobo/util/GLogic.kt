package dev.cidick.sweetbobo.util

import java.util.*

class GLogic {
    private val slotsX = intArrayOf(1, 1, 1)
    private val slots = intArrayOf(1, 1, 1)
    private val slotsY = intArrayOf(1, 1, 1)
    private var theCoins = 0
    private var lines = 1
    private var bet = 0
    private var jackpot = 0
    private var prize = 0
    var hasWon = false
    private var diagonalWon = false
    private val rand = Random()
    private val randomInt: Int
        get() = rand.nextInt(7) + 1

    fun getPosition(i: Int): Int {
        return slots[i] + 5
    }

    private fun checkNumber(x: Int): Int {
        var x = x
        if (x == 0) {
            x = 7
        } else if (x == 8) {
            x = 1
        }
        return x
    }

    val spinResults: Unit
        get() {
            hasWon = true
            diagonalWon = true
            prize = 0
            for (i in slots.indices) {
                slots[i] = randomInt
                val x = slots[i] - 1
                slotsX[i] = checkNumber(x)
                val y = slots[i] + 1
                slotsY[i] = checkNumber(y)
            }
            if (slots[0] == 7 || slots[1] == 7 || slots[2] == 7) {
                diagonalWon = false
                var i = 0
                for (a in slots) {
                    if (a == 7) {
                        i++
                    }
                }
                when (i) {
                    1 -> prize = bet * 5
                    2 -> prize = bet * 10
                    3 -> if (rand.nextInt(20000) == 0) {
                            prize = jackpot
                            jackpot = 0
                        }
                    else -> {}
                }
                theCoins += prize
            } else if (slots[0] == slots[1] && slots[1] == slots[2]) {
                diagonalWon = false
                win(slots)
            } else if (slotsX[0] == slots[1] && slots[1] == slotsY[2]) {
                hasWon = false
                win(slotsX)
            } else if (slotsX[2] == slots[1] && slots[1] == slotsY[0]) {
                hasWon = false
                win(slotsY)
            } else {
                hasWon = false
                diagonalWon = false
                theCoins -= bet
                jackpot += bet
            }
        }

    private fun win(slots: IntArray) {
        when (slots[0]) {
            1 -> prize = bet * 2
            2 -> prize = bet * 3
            3 -> prize = bet * 5
            4 -> prize = bet * 7
            5 -> prize = bet * 10
            6 -> prize = bet * 15
            else -> {}
        }
        theCoins += prize
    }

    val winningPattern: Int
        get() {
            if (diagonalWon) {
                // Diagonal win logic for combination_7
                if (slots[0] == 1 && slots[1] == 5 && slots[2] == 9) {
                    return 1
                } else if (slots[0] == 3 && slots[1] == 5 && slots[2] == 7) {
                    return 2
                }
            } else {
                // Horizontal win logic for combination_7
                if (slots[0] == 7 && slots[1] == 7 && slots[2] == 7) {
                    return slots[0]
                }

                // Horizontal, vertical, and diagonal win logic for combination_1 to combination_6
                var row = -1
                var col = -1
                if (slots[0] in 1..6) {
                    row = (slots[0] - 1) / 3
                    col = (slots[0] - 1) % 3
                }
                if (row != -1 && col != -1 && slots[0] == slots[1] && slots[1] == slots[2]) {
                    // Check for horizontal win
                    if (row == (slots[1] - 1) / 3) {
                        return row + 1 // Return the row for combination_1 to combination_6
                    }
                    // Check for vertical win
                    if (col == (slots[1] - 1) % 3) {
                        return col + 4 // Return the column for combination_1 to combination_6
                    }
                    // Check for diagonal win
                    if (row == col && row == (slots[1] - 1) / 3 && col == (slots[1] - 1) % 3) {
                        return 7 // Return 7 for diagonal win for combination_1 to combination_6
                    }
                }
            }

            // Default: return the first slot
            return slots[0]
        }

    fun betUp() {
        if (bet < 100) {
            bet += 5
            theCoins -= 5
        }
    }

    fun betDown() {
        if (bet > 5) {
            bet -= 5
            theCoins += 5
        }
    }

    // Getters
    fun getTheCoins(): String {
        return theCoins.toString()
    }

    // Setters
    fun setTheCoins(theCoins: Int) {
        this.theCoins = theCoins
    }

    fun getLines(): String {
        return lines.toString()
    }

    fun setLines(lines: Int) {
        this.lines = lines
    }

    fun getBet(): String {
        return bet.toString()
    }

    fun setBet(bet: Int) {
        this.bet = bet
    }

    fun getJackpot(): String {
        return jackpot.toString()
    }

    fun setJackpot(jackpot: Int) {
        this.jackpot = jackpot
    }

    fun getPrize(): String {
        return prize.toString()
    }

    fun setPrize(prize: Int) {
        this.prize = prize
    }
}
