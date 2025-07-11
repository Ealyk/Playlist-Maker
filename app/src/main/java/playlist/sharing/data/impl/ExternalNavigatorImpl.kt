package playlist.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import playlist.sharing.domain.ExternalNavigator
import playlist.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context): ExternalNavigator {

    override fun shareLink(shareAppLink: String) {
        val shareIntent = Intent(Intent.ACTION_SENDTO)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareAppLink)
        context.startActivity(shareIntent)
    }

    override fun openTerms(termsLink: String) {
        val agreementIntent = Intent(Intent.ACTION_VIEW)
        agreementIntent.data = Uri.parse(termsLink)
        context.startActivity(agreementIntent)
    }

    override fun openEmail(email: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email.address))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, email.subjectSupport)
        supportIntent.putExtra(Intent.EXTRA_TEXT, email.messageSupport)
        context.startActivity(supportIntent)
    }

}