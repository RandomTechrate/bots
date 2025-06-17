<p align="center">
  <img src="https://placehold.co/200x200?text=MC-Bots" alt="MC-Bots by CargoX" width="200"/>
</p>

<h1 align="center">MC-Bots by CargoX</h1>
<p align="center">
  <b>⚡ The ultimate Minecraft bot stress tester and automation tool! ⚡</b><br>
  <i>Create and control massive bot armies with advanced movement and flooding patterns.</i>
</p>

---

## ✨ Features

- 🚀 **Fast Bot Creation:** Up to <b>1500 bots</b> join instantly!
- 🤖 **Aggressive Movement Patterns:**
  - ↔️ Forward/back, side-to-side, diagonal, random
  - 🦘 Random jumping
  - 👀 Random looking
- 💥 **Quick Server Flooding:** Bots join and move within seconds
- 🛡️ **Proxy Support:** SOCKS4/SOCKS5 proxies
- 🏷️ **Custom Nicknames:** Realistic or random
- 💬 **Join Messages:** Custom messages/commands on join
- 🔒 **Online Mode:** Premium account support (Microsoft)
- 🛠️ **Highly Configurable:** Tons of CLI options

---

## 🚀 Usage

- Download: [CargoX-BOTS](https://mega.nz/file/UogGgR4Z#9PIKDi_0t4jqsvC4MELgMoBPEg19JTzTngXHATvWd6I)

```bash
java -jar mc-bots-1.2.14.jar -s serverip:port -c 1500
```

---

## 🛠️ Command Line Options

| Option                | Description                                         |
|-----------------------|-----------------------------------------------------|
| `-s, --server`        | Server IP[:port] (required)                         |
| `-c, --count`         | Number of bots (default: 1, max: 1500)              |
| `-d, --delay`         | Connection delay in ms `<min> <max>`                |
| `-r, --real`          | Use real-looking nicknames                          |
| `-n, --nocolor`       | Disable colored chat messages                       |
| `-p, --prefix`        | Set bot nickname prefix                             |
| `-j, --join-msg`      | Join messages/commands (separated by `&&`)          |
| `-l, --proxy-list`    | Path/URL to proxy list file                         |
| `-t, --proxy-type`    | Proxy type: SOCKS4 or SOCKS5                        |
| `-o, --online`        | Use online mode (premium account)                   |
| `-ar, --auto-respawn` | Set autorespawn delay (-1 to disable)               |

---

## 📦 Examples

- **100 bots:**
  ```bash
  java -jar mc-bots-1.2.14.jar -s mc.example.com -c 100
  ```
- **Proxies + real nicks:**
  ```bash
  java -jar mc-bots-1.2.14.jar -s mc.example.com -c 500 -r -l proxies.txt -t SOCKS4
  ```
- **Online mode + join messages:**
  ```bash
  java -jar mc-bots-1.2.14.jar -s mc.example.com -c 1 -o -j "Hello!&&/spawn"
  ```

---

## 🤖 Bot Behavior

- Bots join the server as fast as possible
- After joining, bots perform aggressive movement patterns
- Random jumping and looking around
- No reconnection attempts for faster server flooding
- Movement updates every 20ms for maximum efficiency

---

## 🌐 Proxy List Format

Create a text file with one proxy per line in the format:
```
ip:port
ip:port
ip:port
```

---

## 📝 Notes

- For online mode, you'll need to authenticate with a Microsoft account
- The tool is optimized for maximum server stress
- Use responsibly and only on servers you own or have permission to test

---

## 🏆 Credits

Created by CargoX
Based on the original mc-bots project
