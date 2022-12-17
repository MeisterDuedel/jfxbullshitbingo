# jfxbullshitbingo
A bullshit bingo game written in Java using JavaFX, retrofit2 and Gson. Requires Java 17 or newer (The Windows setup version is bundled with OpenJDK 17). <br>
Bullshit bingo games are loaded as a json file from a web server via retrofit2.<br>
![Screenshot of bullshit bingo](https://github.com/MeisterDuedel/jfxbullshitbingo/blob/main/readme/jfxbsb.png) <br>
### How to play
To load a game, the user has to enter the base URL and the ID of the bullsit bingo and click on load. <br>
Example: The bullshit bingo in the screenshot is stored at "https://christoph-pircher.de/bsb/example.json".<br>
The base URL is "https://christoph-pircher.de/bsb/" and the ID is "example".
### How to host your own bullshit bingo
To host your own bullshit bingo you have to create a .json file which has to be available on a web server.
The json file must have the following format: <br>
```json
{
"title": "Example Bullshit Bingo",
"words": ["Lorem", "ipsum", "dolor", "sit", "amet", "consetetur", "sadipscing", "elitr", "sed", "diam", "nonumy", "eirmod", "tempor", "invidunt", "ut", "labore", "et", "dolore", "magna", "aliquyam", "erat", "voluptua", "At", "vero", "eos", "accusam"]
}
```
