What is it?

Similar to plugins like towny I wanted to create a simplified version, more of the, let the player decide, rather than letting the plugin or an administrator decide.
How it works

Basically, a play can pay money to create a village (if economy is used, or simply it's free) at the chunk they're standing on, and invite players. Players can only be invited to a single village, the residents can then deposit money into the village which the mayor can spend to expand the village.
Future plans

So far the plugin is fairly basic, I wanted to put it on bukkit, see how it goes and if you people like it, I'll add more stuff, you can comment what you'd like to see added below if you wish.

Some of the basic ideas I have now are:

    Regions, which may have one or more Villages in them
    Wars between Villages and/or regions
    Anti Griefing, stealing etc on Villages 

Configuration

Configuration is pretty straight forward, but here's a rehash for those who mightn't understand.

Worlds:
- World
- World_nether
- World_the_end
sql:
  username: root
  password: password
  host: localhost
  database: minecraft
  port: 3306
  use: false
economy: true
colors:
  prefix: '&3[&bVillages&3]'
  error: '&c'
  default: '&3'
  important: '&b'
cost:
  createvillage: 1000.0
  expand: 50.0
townborder: 1
messages:
  entervillage: true
  leavevillage: true

Worlds: List of worlds the plugin is enabled in, I found it annoying that Towny didn't have specific world options. sql: SQL options (SQL is optional, you can use just YML instead). Basic options are username, password, host, database and port for the SQL server, or if you want, you can just use YML. economy: Whether or not to use economy (requires Vault) colors: Change the colors of the messages sent, This is to make it a little easier for the configuration of all the village messages. cost: Cost to do things in the plugin, create villages and how much to expand the village. TownBorder: how many chunks around the edge of the town is unclaimable by another town. Messages:

    entervillage: Whether to get a message when you enter a village
    leavevillage: Whether or not to get a message when you leave a village (and enter the wilderness) 

Commands

List of all commands:

villageadmin: Get Villages stats.
villages: Get a list of Villages.
village: Get information about a specific Village.
createvillage: Creates a new Village at that chunk you're in.
villageinvite: Invites a player to your village.
villageaccept: Invites a player to your village.
villagedeny: Invites a player to your village.

Permissions

   Villages.*: Gives access to all Villages permissions.
   Villages.villageadmin: Allows access to Villages admin command.
   Villages.reload: Allows access to reload Villages configuration file.
   Villages.villages: Allows access to Villages list command.
   Villages.village: Allows access to Villages list command.
   Villages.createvillage: Allows access to the Create Village command.
   Villages.invite: Allows access to the Invite to Village command.
   Villages.accept: Allows access to accept a Village invite.
   Villages.deny: Allows access to deny a Village invite.

