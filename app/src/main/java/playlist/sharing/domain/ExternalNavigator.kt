package playlist.sharing.domain

import playlist.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(shareAppLink: String)
    fun openTerms(termsLink: String)
    fun openEmail(email: EmailData)
}