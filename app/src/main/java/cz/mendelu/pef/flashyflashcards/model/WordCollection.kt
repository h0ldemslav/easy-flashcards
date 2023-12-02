package cz.mendelu.pef.flashyflashcards.model

data class WordCollection(
    var id: Long? = null,
    var name: String,
    var sourceLanguage: String,
    var targetLanguage: String
) {
    companion object {

        fun createFromEntity(entity: WordCollectionEntity): WordCollection {
            return WordCollection(
                id = entity.id,
                name = entity.name,
                sourceLanguage = entity.sourceLanguage,
                targetLanguage = entity.targetLanguage
            )
        }
    }
}