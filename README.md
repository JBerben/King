![ASM](https://img.shields.io/badge/Java-ASM-yellow.svg?style=flat-square)
[![Hits](http://hits.dwyl.io/JBerben/King.svg)](http://hits.dwyl.io/JBerben/King)
[![GitHub Issues](https://img.shields.io/github/issues/JBerben/King.svg?style=flat-square)](https://github.com/JBerben/King/issues)
![Servers](https://img.shields.io/badge/Servers-Runelocus-blue.svg?style=flat-square)
![Downloads](https://img.shields.io/npm/dt/King.svg?style=flat-square)
[![License](https://img.shields.io/badge/license-MIT-red.svg?style=flat-square)](https://opensource.org/licenses/MIT)

![King](https://dl.dropboxusercontent.com/s/yn0rl4kbv06vwjf/KING%20%281%29.png?dl=0)


King is a modified game client for private servers of the popular MMORPG "Runescape". It allows you to create and run your own scripts and exploits. At the moment, this repo only contains the updater for the game client, and not the client itself, but in due time, both of them will be linked together and posted here. (IMAGE HERE). 

_King gives any player an unreasonable advantage, by allowing them to view any variable and invoke any method within the game client_ :smiling_imp:

## How it works
IMAGE HERE
Before the game client can even load, it must first de-obfuscate all of the obfuscated classes, fields and methods within the game client. This is the updaters' job. The updater then maps out all of these variables and renames them, allowing the King client API to read them. Once this is done, the client reflects the game's applet and runs it inside of it's own JFrame.

## How to use

Setup for King is very straight forward

### Installation

```Installation:
> Download repo
> Drag and drop into IDE or compile

> OR, just download the jar file (might be slightly outdated at times)
```      

## Demo

King client in action:

IMAGE HERE

## Todo
- [X] Add auto updating
- [X] Add XML support
- [ ] Add King Client
- [ ] Finish API to write scripts and exploits 

Pull requests are more than welcomed!

## License
Usage is provided under the [MIT License](http://http//opensource.org/licenses/mit-license.php). See LICENSE for the full details.
