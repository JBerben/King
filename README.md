# README IS STILL IN PROGRESS

![King](https://dl.dropboxusercontent.com/s/0rq3fr0dtpvwd4h/NSGIF-header.png?dl=0)

King is a modified game client for private servers of the popular MMORPG "Runescape". It allows you to create and run your own scripts and exploits. At the moment, this repo only contains the updater for the game client, and not the client itself, but in due time, both of them will be linked together and posted here. [_this example_](http://files.parsetfss.com/2677410f-fd15-46aa-a2fa-258c85d4da30/tfss-2215cfe6-03b5-4546-8422-d292f875efb9-whom.gif). 

_King gives any player an unreasonable advantage, by allowing them to view any variable and invoke any method within the game client_ :smiling_imp:

## How it works
![King](https://dl.dropboxusercontent.com/s/nsh0s1shh9fbqpu/NSGIF-HIW.png?dl=0)
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

![King](https://dl.dropboxusercontent.com/s/p02c6l7rzk6mf6m/NSGIF-HT.gif?dl=0)

## Todo
- [X] Add auto updating
- [X] Add XML support
- [ ] Add King Client
- [ ] Finish API to write scripts and exploits 

Pull requests are more than welcomed!

## License
Usage is provided under the [MIT License](http://http//opensource.org/licenses/mit-license.php). See LICENSE for the full details.
