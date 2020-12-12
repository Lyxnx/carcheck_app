package net.lyxnx.carcheck.util

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import androidx.core.content.ContextCompat
import net.lyxnx.carcheck.R

object CustomTabs {

    fun launchUrl(context: Context, url: String) {
        val supportsCustomTabs = getCustomTabsPackages(context, url).isNotEmpty()

        if (supportsCustomTabs) {
            val intent = CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(context, R.color.primary))
                    .build()
            intent.launchUrl(context, Uri.parse(url))
        } else {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    private fun getCustomTabsPackages(context: Context, url: String): List<ResolveInfo> {
        val pm = context.packageManager

        val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        val resolved = pm.queryIntentActivities(activityIntent, 0)
        val supportingPackages = ArrayList<ResolveInfo>()

        for (info in resolved) {
            val serviceIntent = Intent()
            serviceIntent.action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.`package` = info.activityInfo.packageName

            if (pm.resolveService(serviceIntent, 0) != null) {
                supportingPackages.add(info)
            }
        }

        return supportingPackages
    }
}