# 🔐 TokenAuth Mod

<div align="center">

**Minecraft Token Authentication Mod for Fabric 1.21.5**

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.5-00AF4C?style=for-the-badge&logo=minecraft)](https://www.minecraft.net/)
[![Fabric](https://img.shields.io/badge/Fabric-API-DFAF2C?style=for-the-badge)](https://fabricmc.net/)
[![License](https://img.shields.io/badge/License-All--Rights--Reserved-red?style=for-the-badge)](LICENSE)

**Ported to 1.21.5** | **Author: Rect**

</div>

---

## 📖 Description

**TokenAuth** is a client-side mod for Minecraft that allows you to use Bearer tokens for authentication in-game. The mod provides a convenient interface for switching sessions without needing to restart the client.

### ✨ Key Features

- 🔑 **Bearer Token Authentication** — Login to the game using Minecraft Services tokens
- 💾 **Original Session Preservation** — Ability to return to the original session at any time
- 🎨 **Beautiful Interface** — Convenient GUI for working with tokens
- 📱 **Multiplayer Menu Integration** — Quick access to functionality
- 🖼️ **Author Logo** — Display of author information in-game

---

## 🚀 Installation

### Requirements

- **Minecraft**: 1.21.5
- **Fabric Loader**: 0.18.1 or higher
- **Fabric API**: 0.128.2+1.21.5 or higher
- **Java**: 21

### Installation Steps

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.21.5
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) for version 1.21.5
3. Download the latest version of **TokenAuth** from [Releases](../../releases)
4. Place the `.jar` file in the `mods` folder of your `.minecraft` directory
5. Launch the game!

---


## 🔧 Technical Details

### Architecture

The mod uses:
- **Fabric Mod Loader** for game integration
- **Mixin** for accessing Minecraft internal classes
- **Reflection** for working with Session API
- **HTTP Client** for requests to Minecraft Services API

### Project Structure

```
tokenauth/
├── client/
│   ├── TokenAuthManager.java      # Session management
│   ├── TokenAuthScreen.java       # GUI interface
│   ├── LogoRenderer.java          # Logo rendering
│   └── MainClient.java            # Client entry point
├── mixin/
│   ├── MinecraftClientMixin.java  # Session access
│   └── MultiplayerScreenMixin.java # Menu integration
└── Main.java                      # Mod entry point
```

---

## ⚠️ Warnings

- ⚠️ **Use at your own risk** — The mod may violate some servers' rules
- ⚠️ **Token Security** — Do not share your tokens with others
- ⚠️ **Compatibility** — The mod is tested on Fabric 1.21.5, other versions may not work

---

## 📄 License

**All Rights Reserved** — All rights reserved.

This mod is proprietary software. Distribution and modification without the author's permission is prohibited.

---

## 👤 Author

**Rect**

- 📧 Telegram: [@TearsLyn1337](https://t.me/TearsLyn1337)
- 🎮 Minecraft Mod: TokenAuth

---

## 🙏 Acknowledgments

- **Fabric Team** — For the excellent mod loader
- **Mojang** — For Minecraft
- All testers and mod users

---


---

## 🐛 Report an Issue

If you found a bug or have a suggestion:
1. Open an [Issue](../../issues)
2. Describe the problem or suggestion
3. Attach screenshots/logs if necessary

---

<div align="center">

**Made with ❤️ for the Minecraft community**

⭐ **Star this repo if you like the mod!** ⭐

</div>

