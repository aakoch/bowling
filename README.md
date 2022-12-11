# Bowling Application

## Prerequisites

Maven

## Testing

`mvn test`

## Building

`mvn package`

## Running

`Main` is the main class. The main method doesn't actually return an int as the directions say the app should. It prints
out the score to system out. There's another method in that class called oneStep() if a direct String to int conversion
is required.

### Via the command line
```shell
mvn package && java -jar target/bowling-1.0-SNAPSHOT.jar "<< your input >>"
```

For example:
```shell
mvn package && java -jar target/bowling-1.0-SNAPSHOT.jar "9-|9-|9-|9-|9-|9-|9-|9-|9-|9-||X"
```

## Notes

I really started to over-analyze this when I saw that the different representations for a strike, spare, miss, etc. were
under a label of "examples". My implementation would only work with 1 characters delimiters.

I would have liked to have created a better regex for validation. Creating groupings I probably could've extracted the 
frames from a matcher.

JavaDoc is heavy. But the instructions said you'd be looking at it, so I made sure to include it where I saw fit. You can find
it in the `docs` folder on GitHub.

I have a frame number as part of the frame with the idea that it could be used for display or debugging or whatnot. 
Idea was to have it display something like:

```text
 _____________________________________________________________
|__1__|__2__|__3__|__4__|__5__|__6__|__7__|__8__|__9__|__10___|
|    X|  8|/|  3|6|    X|  9|/|  8|/|    X|  9|/|  9|-|  X|X|X|
|  20 |  33 |  42 |  62 |  80 | 100 | 120 | 139 | 148 |  178  |
'-----'-----'-----'-----'-----'-----'-----'-----'-----'-------'
```