# ArtBookKotlin

ArtBookJava projesinin Kotlin ve Jetpack Compose tabanli versiyonudur.

## Proje Aciklamasi

ArtBookKotlin, kullanicinin sanat eserlerini listeleyebildigi, yeni eser ekleyebildigi ve secilen eserin detayini gorebildigi bir Android uygulamasidir.

Bu surumde Java + XML yaklasimindan Kotlin + Compose yaklasimina gecis yapilmistir. Mevcut islevler korunurken UI katmani modern Compose yapisina tasinmistir.

## Temel Ozellikler

- Eser listesi goruntuleme
- Eser detay goruntuleme
- Galeriden gorsel secerek yeni eser ekleme
- SQLite tabanli kalici veri saklama
- Compose Navigation ile ekran gecisleri

## Teknolojiler

- Kotlin
- Jetpack Compose (Material 3)
- Navigation Compose
- Coil Compose
- Android SQLite (openOrCreateDatabase)

## Proje Yapisi

```text
ArtBookKotlin/
|- app/
|  |- src/main/java/com/gursel/artbookkotlin/
|  |  |- MainActivity.kt     # Uygulama giris noktasi
|  |  |- Screens.kt          # Compose ekranlari ve navigation
|  |  |- DBHelper.kt         # SQLite islemleri
|  |  |- Art.kt              # Veri modeli
|  |  \- ui/theme/           # Tema dosyalari
|  \- build.gradle.kts       # Uygulama bagimliliklari
|- gradle/
|- build.gradle.kts
\- settings.gradle.kts
```

## Ekranlar ve Akis

1. Liste Ekrani
   - Veritabanindaki eserleri listeler.
   - Bir ogeye tiklaninca detay ekranina gider.

2. Yeni Eser Ekrani
   - Eser adi, sanatci adi ve yil girilir.
   - Galeriden gorsel secilir.
   - Kaydet ile SQLite veritabanina yazilir.

3. Detay Ekrani
   - Secilen eserin adi, sanatci bilgisi, yil ve gorseli gosterilir.

## Java -> Kotlin + Compose Gecis Ozeti

Eski yapi (Java/XML):
- RecyclerView + Adapter
- XML layout dosyalari
- Activity tabanli ekran akisi

Yeni yapi (Kotlin/Compose):
- LazyColumn ile listeleme
- Composable tabanli UI
- Navigation Compose ile route tabanli ekran gecisi

## Kurulum ve Calistirma

Gereksinimler:
- Android Studio (guncel surum)
- Android SDK 24+

PowerShell ile build:

```powershell
cd 'C:\Users\gurse\AndroidStudioProjects\ArtBookKotlin'
.\gradlew.bat assembleDebug
```

Opsiyonel kurulum (cihaz/emulator bagliysa):

```powershell
cd 'C:\Users\gurse\AndroidStudioProjects\ArtBookKotlin'
.\gradlew.bat installDebug
```

## Git Branch Bilgisi

Bu migration calismasi su branch uzerinden ilerletilmistir:
- `migration/compose`

## Notlar

- Bu proje functional parity odakli migration calismasidir.
- Sonraki adimda `ViewModel` + `Room` mimarisine gecis ayri bir gelistirme olarak ele alinabilir.
