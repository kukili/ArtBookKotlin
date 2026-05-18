# ArtBookKotlin

TR: ArtBookJava projesinin Kotlin ve Jetpack Compose tabanli versiyonudur.

EN: Kotlin + Jetpack Compose based version of the ArtBookJava project.

## Proje Aciklamasi (TR)

ArtBookKotlin, kullanicinin sanat eserlerini listeleyebildigi, yeni eser ekleyebildigi ve secilen eserin detayini gorebildigi bir Android uygulamasidir.

Bu surumde Java + XML yaklasimindan Kotlin + Compose yaklasimina gecis yapilmistir. Mevcut islevler korunurken UI katmani modern Compose yapisina tasinmistir.

## Project Overview (EN)

ArtBookKotlin is an Android app where users can list artworks, add new artworks, and view artwork details.

In this version, the project is migrated from Java + XML to Kotlin + Jetpack Compose while preserving the core behavior.

## Temel Ozellikler / Core Features

- TR: Eser listesi goruntuleme
  EN: List artworks
- TR: Eser detay goruntuleme
  EN: View artwork details
- TR: Galeriden gorsel secerek yeni eser ekleme
  EN: Add a new artwork by selecting an image from gallery
- TR: SQLite tabanli kalici veri saklama
  EN: Persist data with SQLite
- TR: Compose Navigation ile ekran gecisleri
  EN: Screen routing with Navigation Compose

## Teknolojiler / Tech Stack

- Kotlin
- Jetpack Compose (Material 3)
- Navigation Compose
- Coil Compose
- Android SQLite (`openOrCreateDatabase`)

## Proje Yapisi / Project Structure

```text
ArtBookKotlin/
|- app/
|  |- src/main/java/com/gursel/artbookkotlin/
|  |  |- MainActivity.kt     # App entry point
|  |  |- Screens.kt          # Compose screens and navigation
|  |  |- DBHelper.kt         # SQLite operations
|  |  |- Art.kt              # Data model
|  |  \- ui/theme/           # Theme files
|  \- build.gradle.kts       # App dependencies
|- screenshots/              # Screenshots and demo GIF
|- gradle/
|- build.gradle.kts
\- settings.gradle.kts
```

## Ekran Goruntuleri / Screenshots

> Not: Asagidaki gorselleri eklemek icin dosyalari `screenshots/` klasorune koyun.

### Ana Liste / Main List
![Main List](screenshots/main-list.png)

### Yeni Eser / Add New Artwork
![Add New Artwork](https://github.com/user-attachments/assets/7e12ccf3-aae8-4b77-9137-4e37d1d6d5f0)

### Detay Ekrani / Detail Screen
![Detail Screen](screenshots/detail.png)

## Kisa Demo / Short Demo (GIF)

![App Demo](screenshots/demo.gif)

## Kurulum ve Calistirma / Setup and Run

Gereksinimler / Requirements:
- Android Studio (guncel surum / recent version)
- Android SDK 24+

PowerShell ile build / Build with PowerShell:

```powershell
cd 'C:\Users\gurse\AndroidStudioProjects\ArtBookKotlin'
.\gradlew.bat assembleDebug
```

Opsiyonel kurulum (cihaz/emulator bagliysa) / Optional install (device/emulator connected):

```powershell
cd 'C:\Users\gurse\AndroidStudioProjects\ArtBookKotlin'
.\gradlew.bat installDebug
```

## Migration Ozeti / Migration Summary

Eski yapi (Java/XML) / Old architecture:
- RecyclerView + Adapter
- XML layout dosyalari
- Activity tabanli ekran akisi

Yeni yapi (Kotlin/Compose) / New architecture:
- LazyColumn ile listeleme
- Composable tabanli UI
- Navigation Compose ile route tabanli ekran gecisi

## Branch Bilgisi / Branch Info

- `migration/compose`

## Notlar / Notes

- TR: Bu proje functional parity odakli migration calismasidir.
- EN: This migration focuses on functional parity.
- TR: Sonraki adimda `ViewModel` + `Room` mimarisine gecis ayri bir gelistirme olarak ele alinabilir.
- EN: Migrating to `ViewModel` + `Room` can be planned as a follow-up improvement.
