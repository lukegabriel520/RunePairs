RunePairs
=========


OVERVIEW
--------

RunePairs is a memory card game for Android set in the world of Skyrim.
You flip cards to find matching pairs of Skyrim city icons before the
timer runs out. The whole thing has a Skyrim feel to it, from the fonts
and artwork to the music playing in the background.

You need an account to play. Registration and login are handled locally
on your device, so no internet connection is needed to get started.


FEATURES
--------

Login and Sign Up
  Create an account with your email and a password. Your session is saved
  on your device so you stay logged in between plays.

Three Difficulty Levels
  Casual  - 4 pairs of cards, 2 minutes on the clock
  Adept   - 6 pairs of cards, 75 seconds on the clock
  Hell    - 9 pairs of cards, 45 seconds on the clock

Memory Card Game
  Cards are laid out face down on a grid. Tap two at a time to flip them.
  If they match, they stay face up and dim out. If they do not match,
  they flip back over. Match every pair before the timer hits zero to win.

Scoring
  You earn 100 points for each matched pair. Finish the round early and
  you get a time bonus on top of that. Your flip count is tracked too,
  so you can see how clean your run was.

Pause
  Tap the pause button mid-game to take a break. The music stops and
  you can resume whenever you are ready.

City Catalog
  Browse all nine Skyrim cities used in the game. Each entry shows the
  city name, its Jarl, the local environment, and a short description.
  Tap the arrows to cycle through them.

Leaderboards
  Your scores are saved on your device and listed on the leaderboard.
  You can filter by difficulty to see your best runs per level. The
  board keeps your top 20 scores and sorts them by score, then time,
  then number of flips.

Settings
  Toggle sound effects and background music on or off. You can also
  sign out from this screen.


HOW TO RUN
----------

What you need:
  - Android Studio (Hedgehog or newer is a good pick)
  - A device or emulator running Android 7.0 (API 24) or higher
  - A google-services.json file, which is already included in the app folder

Steps:
  1. Clone or download this repository to your machine.
  2. Open the project in Android Studio.
  3. Let Gradle sync and pull down the dependencies. This can take a minute
     or two the first time.
  4. Connect an Android device with USB debugging on, or start an emulator.
  5. Press the Run button or hit Shift + F10.
  6. The app will build, install, and launch on your device.

That is it. No extra setup or config files needed beyond what is already
in the repo.


CHORES LEFT TO DO
-----------------

- The sound effects and music toggles in Settings do not actually work
  yet. Flipping them on or off does not change the audio. The toggle
  state lives only in memory and resets when you leave the screen.

- Login does not check passwords. Right now the app only looks up whether
  the email exists. Anyone who knows a registered email address can log in
  with any password at all. This needs to be fixed before the app is shared
  with real users.

- Firebase is set up in the project but is not connected to anything yet.
  The plan seems to be to sync scores online at some point, but for now
  everything stays on the local device.

- There is no shared online leaderboard. Scores are stored per device,
  so you cannot compare runs with other players.

- There is no way to delete your account or clear your score history from
  inside the app.
