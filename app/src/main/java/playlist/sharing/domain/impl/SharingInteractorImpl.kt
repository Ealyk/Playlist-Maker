package playlist.sharing.domain.impl

import android.content.Context
import com.ealyk.playlistmaker2.R
import playlist.sharing.domain.ExternalNavigator
import playlist.sharing.domain.SharingInteractor
import playlist.sharing.domain.model.EmailData

class SharingInteractorImpl (
    private val externalNavigator: ExternalNavigator,
    private val context: Context,
): SharingInteractor {

    override fun shareApp() {
        return externalNavigator.shareLink(getShareAppLink(), shareDescription())
    }

    override fun openTerms() {
        return externalNavigator.openTerms(getTermsLink(), termsDescription())
    }

    override fun openSupport() {
        return externalNavigator.openEmail(getSupportEmailData(), supportDescription())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.share_uri)
    }

    private fun shareDescription(): String {
        return context.getString(R.string.share)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            address = context.getString(R.string.email),
            subjectSupport = context.getString(R.string.subject_support),
            messageSupport = context.getString((R.string.message_support))
        )
    }
    private fun supportDescription(): String {
        return context.getString(R.string.support)
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.agreement_uri)
    }
    private fun termsDescription(): String {
        return context.getString(R.string.agreement)
    }

}