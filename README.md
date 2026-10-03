# Server Hub Android

تطبيق Android جاهز للاتصال بواجهة Server Hub التي تعمل في Termux.

## المزايا
- واجهة WebView داخل التطبيق، لذلك محتوى الموقع يتحدث مع ملفات Server Hub مباشرة.
- عنوان السيرفر قابل للتغيير، والافتراضي `http://127.0.0.1:8080/`.
- عرض حالة الاتصال وIP الشبكة.
- حفظ آخر عنوان استخدمته.
- فتح وتصفح تسجيل الدخول والملفات من داخل التطبيق.
- دعم JavaScript وLocal Storage للمواقع الحديثة.
- لا يحتاج AppCompat أو Material Components، لتقليل مشاكل الاعتماديات.

## بناء APK

من Android Studio أو AndroidIDE:

```bash
./gradlew assembleDebug
```

الملف الناتج:

`app/build/outputs/apk/debug/app-debug.apk`

ومن GitHub Actions شغّل workflow باسم `Build Server Hub APK`، وسيظهر APK في Artifacts.

## إعداد Server Hub

إذا كان Server Hub يعمل على نفس الهاتف استخدم:

`http://127.0.0.1:8080/`

إذا أردت الاتصال من هاتف آخر على نفس Wi-Fi، استخدم IP الهاتف الذي يشغل Termux، مثل:

`http://172.18.89.22:8080/`
