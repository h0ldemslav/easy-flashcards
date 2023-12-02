package cz.mendelu.pef.flashyflashcards.di

import cz.mendelu.pef.flashyflashcards.FlashyFlashcardsApplication
import cz.mendelu.pef.flashyflashcards.database.FlashyFlashcardsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(): FlashyFlashcardsDatabase =
        FlashyFlashcardsDatabase.getDatabase(FlashyFlashcardsApplication.appContext)
}