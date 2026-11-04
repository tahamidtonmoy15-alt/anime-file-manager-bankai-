# Runs only if the Capacitor template has no FileProvider
p = "android/app/src/main/AndroidManifest.xml"
s = open(p, encoding="utf8").read()
prov = ('<provider android:name="androidx.core.content.FileProvider" '
        'android:authorities="${applicationId}.fileprovider" android:exported="false" '
        'android:grantUriPermissions="true"><meta-data '
        'android:name="android.support.FILE_PROVIDER_PATHS" android:resource="@xml/file_paths"/></provider>')
s = s.replace("</application>", prov + "</application>", 1)
open(p, "w", encoding="utf8").write(s)
