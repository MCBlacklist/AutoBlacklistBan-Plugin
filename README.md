# AutoBlacklistBan

**AutoBlacklistBan** is a Minecraft **Paper plugin** that automatically bans any player who appears in a configured blacklist. Useful for server admins who want to keep certain users off their server without constantly monitoring logins.

--

[**Community Blacklist Website**](https://mcblacklist.com/)

[**Community Blacklist Discord**](https://discord.gg/RjahuQgNXV)

[**Developer Support Discord**](https://discord.gg/hYw2Tdv5BH)

**[Our Github](https://github.com/MCBlacklist/AutoBlacklistBan-Plugin/)**

**[Our Modrinth](https://modrinth.com/organization/mcblacklist-dev)**

---

## Features

- Automatically ban players when they login if they are on a blacklist.  
- Compatible with **Paper** (and likely other Bukkit/Spigot forks that support same API).  
- Easy configuration (add / remove blacklisted names or UUIDs).  
- Optional messages / logging so admins know who was banned and why.

---

## Installation

1. Download the latest compiled `.jar` of AutoBlacklistBan.  
2. Place the `.jar` into your server's `plugins` folder.  
3. Start or reload the server so that the plugin generates its config files.
4. Now you may add/remove your staff members using the "/blacklistnotify grant|remove <name>" command
5. If you'd like to you may exempt certain players from the ban using the "/exemptblacklist <name>" command
6. Both of the above may be edited using `exempt.yml` and `notify.yml`!
