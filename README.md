# MC-Bots by CargoX

A powerful Minecraft bot tool for server stress testing and automation. This tool allows you to create and control multiple Minecraft bots with advanced movement patterns and behaviors.

## Features

- **Fast Bot Creation**: Create up to 1500 bots simultaneously
- **Aggressive Movement Patterns**:
  - Forward/back movement
  - Side-to-side movement
  - Diagonal movement
  - Random direction movement
  - Random jumping
  - Random looking
- **Quick Server Flooding**: Bots join and start moving within seconds
- **Proxy Support**: Use SOCKS4/SOCKS5 proxies for your bots
- **Custom Nicknames**: Generate random or real-looking nicknames
- **Join Messages**: Send custom messages when bots join
- **Online Mode Support**: Use premium accounts with Microsoft authentication

## Usage

Basic usage:
```bash
java -jar mc-bots-1.2.14.jar -s serverip:port -c 1500
```

### Command Line Options

- `-s, --server`: Server IP[:port] (required)
- `-c, --count`: Number of bots to create (default: 1, max: 1500)
- `-d, --delay`: Connection delay in ms <min> <max>
- `-r, --real`: Generate real-looking nicknames
- `-n, --nocolor`: Disable colored chat messages
- `-p, --prefix`: Set bot nickname prefix
- `-j, --join-msg`: Join messages/commands (separated by &&)
- `-l, --proxy-list`: Path/URL to proxy list file
- `-t, --proxy-type`: Proxy type (SOCKS4 or SOCKS5)
- `-o, --online`: Use online mode (premium account)
- `-ar, --auto-respawn`: Set autorespawn delay (-1 to disable)

### Examples

1. Basic usage with 100 bots:
```bash
java -jar mc-bots-1.2.14.jar -s mc.example.com -c 100
```

2. Using proxies with real nicknames:
```bash
java -jar mc-bots-1.2.14.jar -s mc.example.com -c 500 -r -l proxies.txt -t SOCKS4
```

3. Using online mode with custom join messages:
```bash
java -jar mc-bots-1.2.14.jar -s mc.example.com -c 1 -o -j "Hello!&&/spawn"
```

## Bot Behavior

- Bots join the server as fast as possible
- After joining, bots perform aggressive movement patterns
- Random jumping and looking around
- No reconnection attempts for faster server flooding
- Movement updates every 20ms for maximum efficiency

## Proxy List Format

Create a text file with one proxy per line in the format:
```
ip:port
ip:port
ip:port
```

## Notes

- For online mode, you'll need to authenticate with a Microsoft account
- The tool is optimized for maximum server stress
- Use responsibly and only on servers you own or have permission to test

## Credits

Created by CargoX
Based on the original mc-bots project
