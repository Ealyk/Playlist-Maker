package playlist.sharing.data.impl


import android.content.Context
import android.content.Intent
import android.net.Uri
import playlist.sharing.domain.ExternalNavigator
import playlist.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context): ExternalNavigator {

    override fun shareLink(shareAppLink: String, shareDescription: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareAppLink)
        context.startActivity(
            Intent.createChooser(shareIntent, shareDescription)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override fun openTerms(termsLink: String, termsDescription: String) {
        val agreementIntent = Intent(Intent.ACTION_VIEW)
        agreementIntent.data = Uri.parse(termsLink)
        context.startActivity(
            Intent.createChooser(agreementIntent, termsDescription)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )

    }

    override fun openEmail(email: EmailData, supportDescription: String) {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email.address))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, email.subjectSupport)
        supportIntent.putExtra(Intent.EXTRA_TEXT, email.messageSupport)
        context.startActivity(
            Intent.createChooser(supportIntent, supportDescription)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

}