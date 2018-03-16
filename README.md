# DFW
DFW works by allowing any application to call it using a basic intent.

# Calling
Calling via ADB looks like this:
```
am broadcast -a <primary> --es <secondary> "<value>" ... -n com.draco.dfw/.Exec
```
For example:
```
am broadcast -a wm --es size "1080x2220" --es density "480" --es overscan "50,50,50,50" -n com.draco.dfw/.Exec
```

# Functions
The primary call is the intent action. The secondary calls are called using extra data.
```
- wm
	- size
	- density
	- overscan
```