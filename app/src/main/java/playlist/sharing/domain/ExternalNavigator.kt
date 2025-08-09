package playlist.sharing.domain


import playlist.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(shareAppLink: String, shareDescription: String)
    fun openTerms(termsLink: String, termsDescription: String)
    fun openEmail(email: EmailData, supportDescription: String)
}