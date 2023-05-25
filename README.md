# koreanchat

The mod which supports custom IME implementation to TextField, writting book, sign.

## Third-party implementation information

### Custom Language Support
- You can add custom implementation of `me.ddayo.koreanchat.client.ime.IIM`.
- See also: `me.ddayo.koreanchat.client.ime.EnglishIM`(English implementation)

This method is for backspace implementation. You should implement checker of given character is calculated by this IME.
```kotlin
fun isRegion(ch: Char): Boolean
```

This method is implementation of backspace. The parameter is the raw string to modify and cursor location. The return is the new string and amount of string to delete.

If given string is "asdf" and the cursor is 1 then, if you want to modify string to 12sdf, the return should be "12" to 1
```kotlin
fun backspace(str: String, pos: Int): Pair<String, Int>
```
This method is very similar with backspace, but, it accepts the new inputted characters. 

The modify paramater means is it sometimes need to modify the previous input. See also: KoreanIM
```kotlin
fun insert(str: String, ch: Char, pos: Int, modify: Boolean): Pair<String, Int>
```
This method is for convertion keycode to character.
```kotlin
fun ch(kp: Int, modifier: Int): Char
```

### Custom InputField Support
- In this case, you can implement Custom IIMEAcceptable.

The text and cursor property is the current state. You should use custom getter of those properties.

The accept method is for to apply the output from IME. parameter str is for new written text, rewritten is for the text to delete from current cursor.
It is able to assume that rewritten is always same or greather then cursor location.

See also: `me.ddayo.koreanchat.client.ime.acceptable.TextFieldAcceptable`
