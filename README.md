# MythicMobsProtection

You must be confused by the skill-spawned mobs in MythicMobs!  
They can bypass PVP limitation via Residence or other protection plugins.  
Then they kill players even animals!

This plugin cancelled damage on above situation. So you can use your skills freely!

## Compatibility

+ [x] Java 8
+ [ ] MythicMobs 4 *not tested yet*
+ [x] MythicMobs 5
+ [x] [Residence](https://www.spigotmc.org/resources/11480)
+ [x] [Guilds](https://www.spigotmc.org/resources/110931)

PRs welcome for more protection plugins.

## Commands

Operators and console only.

Command alias: `mythicmobsprotection`, `mythicmobsext`, `mmprotection`, `mmprotect` `mmp`

| Command                       | Description                                    |
|-------------------------------|------------------------------------------------|
| `/mmp skill <player> <skill>` | Cast spell for player without any message tips |
| `/mmp toggle`                 | Toggle protection status for test              |

## Build

Please run gradle with `Java 17`, build result is still `8` compatibly

```shell
./gradlew build
```
