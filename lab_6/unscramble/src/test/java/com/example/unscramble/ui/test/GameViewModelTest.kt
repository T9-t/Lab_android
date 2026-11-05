package com.example.unscramble.ui.test

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.GameViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class GameViewModelTest {
    private val viewModel = GameViewModel()
    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
        private const val  FINAL_NO_OF_WORDS = MAX_NO_OF_WORDS
        private const val MAX_SCORE = SCORE_INCREASE * MAX_NO_OF_WORDS
    }

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset()  {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value
        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        val nonCorrectPlayerWord = "?"

        viewModel.updateUserGuess(nonCorrectPlayerWord)
        viewModel.checkUserGuess()

        val currentGameUiState = viewModel.uiState.value
        assertTrue(currentGameUiState.isGuessedWordWrong)
    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val currentGameUiState = viewModel.uiState.value

        assertNotNull(currentGameUiState.currentScrambledWord)
        assertTrue(currentGameUiState.currentScrambledWord.isNotEmpty())

        assertEquals(1, currentGameUiState.currentWordCount)
        assertEquals(0, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        var currentGameUiState = viewModel.uiState.value
        var isOver = currentGameUiState.isGameOver
        var correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        while (!isOver) {
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()

            currentGameUiState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
            isOver = currentGameUiState.isGameOver
        }
        currentGameUiState = viewModel.uiState.value
        assertEquals(MAX_SCORE, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_WordSkipped_ScoreUnchangedAndWordCountIncreased() {
        var currentGameUiState = viewModel.uiState.value
        var isOver = currentGameUiState.isGameOver

        while (!isOver) {
            viewModel.skipWord()

            currentGameUiState = viewModel.uiState.value
            isOver = currentGameUiState.isGameOver
        }
        currentGameUiState = viewModel.uiState.value
        assertEquals(0, currentGameUiState.score)
        assertEquals(FINAL_NO_OF_WORDS, currentGameUiState.currentWordCount)
    }
}