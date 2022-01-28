package com.example.memorygame.models

import com.example.memorygame.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize, private val customImages: List<String>?){

    val cards : List<MemoryCard>
    var numPairsFound = 0

    private var numCardFlips = 0
    private var indexOfSingleSelectedCard : Int? = null

    init {

        if(customImages==null) {
            val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getPairs())
            val randomizedImages = (chosenImages + chosenImages).shuffled()
            cards = randomizedImages.map { MemoryCard(it) }
        }
        else{
            val randomizedImages = (customImages+customImages).shuffled()
            cards = randomizedImages.map{ MemoryCard(it.hashCode(),it) }
        }
    }


    fun filpCard(position: Int) : Boolean{
        numCardFlips++
        val currentCard = cards[position]

        // There are only three possible cases

        // 1 : There is no card flipped yet = flip the current card
        // 2 : There is one card flipped yer = flip the current card + check if the two cards match
        // 3 : Restore the previous two cards and then filp the currently clicked card.

        var foundMatch = false

        if(indexOfSingleSelectedCard==null){
            // 0 or 2 cards previously flipped over.
            restoreCards()
            indexOfSingleSelectedCard = position
        }
        else{
            // exactly 1 card  is previously flipped over

            foundMatch = checkForMatch(indexOfSingleSelectedCard!!,position)
            indexOfSingleSelectedCard= null
        }
        currentCard.isFaceUp = !currentCard.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(position1 : Int, position2: Int): Boolean {

        if(cards[position1].identifier!=cards[position2].identifier){
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {

        for(card in cards){
            if(!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        if(numPairsFound == boardSize.getPairs()){
            return true
        }
        else{
            return false
        }
    }

    fun isCardFaceUp(position: Int): Boolean {
        if(cards[position].isFaceUp){
            return true
        }
        else{
            return false
        }
    }

    fun getNumMoves() : Int{

        return numCardFlips/2
    }
}