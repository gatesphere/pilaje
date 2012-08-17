pilaje
======

Pilaje ("pillage") is "pila-on-java-extended", a super-set of [pila](https://github.com/gatesphere/pila), written in Java

Overview
--------

Pilaje aims to be an extended version of [pila](https://github.com/gatesphere/pila), 
written in pure Java.

Here's a factorial macro:

    :fac dup 0 = #(dup 1 - fac *) #(pop 1) if
    
Here's how you use it:

    $main[0]> 6 fac .
    720
    
Here's a Fibonacci calculator:

    :fib dup 1 <= #(1 - dup fib swap 1 - fib +) #(pop 1) if
    
And an example:
    
    $main[0]> 6 fib .
    13

String manipulation can be neat too:
  
    $main[0]> "foo" "bar" + .
    "foobar"
    $main[1]> "baz" 3 * ...
    ["foobar", "bazbazbaz"]<=
    $main[2]> + .
    "foobarbazbazbaz"
    $main[1]> !bye
    goodbye  

Anonymous macros open up the possibilities for meta-programming.  Here's a particularly
*bad* example:

    :a #(:b a) call b

Requirements
------------

**Running**
To run pilaje, you need a compiled version of the source, and a recent
JRE/JDK.  It is developed against 1.7.0, so YMMV for other versions.

Compiling
---------
You'll need the following to compile pilaje:

  * A recent JDK. It is developed against 1.7.0, so YMMV for other versions.
  * A bash shell, or some desire to read bash scripts and translate for your platform.

To compile using the provided bash scripts, just run a `./build.sh`.  This will create
`pilaje.jar`, which can be run with `java -jar pilaje.jar <your args here>` or with
the provided `./run.sh <your args here>`.  Use `-h` for more information on available
command line switches.  You can run a file in non-interactive script mode by supplying
its filename as the last parameter on the command line, i.e. `./run.sh some/file.pilaje`.

New Features
------------
Pilaje adds several new features to pila, aside from being written in Java
and more portable, such as:

  * An arbitrary number of stacks (up from just one in pila)
  * Stacks changable on the fly ($stackname)
  * Moving variables between stacks (->, -->, <-, <--)
  * Delete a macro/stack (~name)
  * New builtins:
    * sz
    * syscall
    * pause
    * !stacks
    * !export (planned)
  * Macro and ntack name validation
  * Proper string handling (including escaped characters and space preservation...)
  * User input (planned)
  * File I/O (planned)
  * Network I/O (planned)
  * Concurrency (planned)

Pila code in pilaje
-------------------
Pilaje is a superset of pila, meaning that any pila code will run just fine in pilaje.

Documentation
-------------
**Concepts**
Pilaje is a stack based language, meaning that all operations in pila are operations
to manipulate the stack.  There are arbitrarily many stacks to manipulate, and no 
concept of anything such as a variable.  There are however macros and anonymous 
macros to make your life easier.

**Words**
Stack based languages operate on the concept of words, which can be seen as unary
functions--with an implicit argument being the stack.  For example, the word `3`
is a function which pushes the `3` onto the stack.  The word `...` prints the
stack in text form.  And the word `-` pops the top two elements from the stack
and subtracts the first popped element from the second.  There's a more in-depth
look at words below.

**Macros**
Macros are placeholders for a collection of words.  Using macros in clever ways,
you can significantly reduce the cognitive burden on yourself when writing programs.

**Data types**
Pilaje recognizes and works with 3 distinct data types, which is really all you need.
Pilaje will work with numbers (integers and floating point, autoconverted on the fly),
character strings (surrounded with `"` characters), and boolean values (`true` and
`false`).

Pilaje supports 4 different bases for numbers, though they are all converted to decimal
before being pushed onto the stack.  To use a number in base 2, 8, or 16, prefix it
with the appropriate token:

Base | Prefix
-----|-------
2    | 0b
8    | 0o
10   | (none)
16   | 0x

Pilaje is also smart when it comes to types: `+` and `*` change their behavior when
appropriate to work on numbers and strings!

**Builtins**
The following words are built in as a part of pilaje, and cannot be redefined
or deleted.  All words operate on the current stack.

Word    | Description
--------|------------
.       | Print the top of the stack.
...     | Print the whole stack.
cls     | Empty the stack completely.
sz      | Push the current size of the stack on top of the stack.
dup     | Duplicate the top of the stack.
pop     | Discard the top of the stack.
swap    | Exchange the first two items of the stack.
rot     | Move the third item in the stack to the top of the stack.
-rot    | Move the top of the stack into the third position.
over    | Push a copy of the second item in the stack to the top of the stack.
nip     | Remove the second item in the stack.
tuck    | Copy the top element of the stack to the third position.
2dup    | Same as dup, but copies the top two elements.
2pop    | Same as pop, but pops the top two elements.
2swap   | Same as swap, but operates on pairs of elements.
2rot    | Same as rot, but operates on pairs of elements.
2-rot   | Same as -rot, but operates on pairs of elements.
2over   | Same as over, but operates on pairs of elements.
2nip    | Same as nip, but operates on pairs of elements.
2tuck   | Same as tuck, but operates on pairs of elements.
+       | Pops the top two items and pushes their sum onto the stack.  If at least one of the elements is a string, + performs concatenation.
-       | Pops the top two items and pushes their difference onto the stack.
*       | Pops the top two items and pushes their product onto the stack.  If one of the elements is a string, * performs string multiplication.
/       | Pops the top two items and pushes their quotient onto the stack.
%       | Pops the top two items and pushes their modulo onto the stack.
<<      | Pops the top two items (m, n) from the stack and pushes (m << n) to the stack.
>>      | Pops the top two items (m, n) from the stack and pushes (m >> n) to the stack.
=       | Pops the top two items from the stack and pushes true if they're equal, false otherwise.
>       | Pops the top two items (m, n) from the stack and pushes true if m > n, false otherwise.
<       | Pops the top two items (m, n) from the stack and pushes true if m < n, false otherwise.
>=      | Pops the top two items (m, n) from the stack and pushes true if m >= n, false otherwise.
<=      | Pops the top two items (m, n) from the stack and pushes true if m <= n, false otherwise.
and     | Pops the top two items (m, n) from the stack and pushes true if (m && n) == true, false otherwise.
or      | Pops the top two items (m, n) from the stack and pushes true if (m && n) == true, false otherwise.
not     | Pops the top item (n) from the stack and pushes true if n == false, false otherwise.
nop     | Does absolutely nothing (no operation).
call    | Pops the top item from the stack and attempts to evaluates it as code.
syscall | Pops the top item, runs it as a system call, and pushes the return value onto the stack.
pause   | Pops the top item from the stack, and pauses for that many milliseconds.
if      | Pops the top three items from the stack (c, t, e), and if c == true, evaluates t, otherwise evaluates e.
!bye    | Exits the program.
!macros | Lists defined macros.
!stacks | Lists all stacks and their current size.
!import | Pops the top item (n) from the stack and reads in the script file whose name is n.

**Creating new stacks**
Pilaje creates one stack upon startup, `$main`.  You can create as many other stacks
as you want or need by coming up with a name for them, and putting a `$` in front of
it.  For example, `$a` will create a stack called `$a` and switch to it.  The stack is
created automatically if it doesn't already exist.  If it does, `$a` will just switch
to the `$a` stack.  Simple enough, right?

**Deleting stacks**
You can delete stacks when you no longer need them by placing a `~` in front of
their name, such as `~$a`, which will delete the `$a` stack.

**Transferring information between stacks**
You can move data between stacks with 4 built in transfer words.  Here, `$stackname`
means the name of the target stack.

Word          | Action
--------------|-------
->$stackname  | Pop the current stack and push that to the target stack
-->$stackname | Copy the top of the current stack and push that to the target stack
<-$stackname  | Pop the target stack and push that to the current stack
<--$stackname | Copy the top of the target stack and push that to the current stack

**Defining macros**
You can define a macro by prefacing it's name with a `:` character, and then 
following it's name immediately by it's definition.  For example, here's a 
macro (titled `hello`) that prints "Hello, world!" to the screen and then returns
the stack to it's previous state:

    :hello "Hello, world!" . pop
    
This macro will now be called whenever the word `hello` is encountered in the input.

**Redefining macros**
You can redefine a macro in the same manner as you defined it originally.  Note 
that you will get a warning telling you that you have redefined the macro.

**Deleting macros**
You can delete a macro by putting a `~` in front of it's name.  For example, `~hello`
will delete the `hello` macro defined above.

**Anonymous macros**
Anonymous macros allow you to push a body of code onto the stack directly, instead
of pushing the literal elements.  Following the `hello` example from above, you 
can push the call to `hello` itself onto the stack by doing this:

    #(hello)
    
Printing the stack, we now see this:

    [hello]<=

Meaning that the word `hello` has been pushed onto the stack.  This is useful for
many things, including the branching word `if`, and the `call` word, which pops the
top of the stack and attempts to execute it directly, such that

    #(hello) call
   
Is functionally equivalent to

    hello
   
in everything but semantics.

Anonymous macros can be used to push entire lines of input to the stack, too:

    #(2 dup = . pop)
    
This will push the code `2 dup = . pop` to the stack as a *single element*,
and that entire expression will be evaluated one word at a time when you do a
`call` on it.

Anonymous macros can also be nested:

    #(1 2 3 #(- +))
    
Such that the first `call` will push `1`, `2`, and `3` to the stack, as well as
the anonymous macro `- +`, and the second `call` will evaluate this second macro.

**Conditionals**
Conditional execution is handled by the `if` word.  The `if` word expects at least
3 items on the stack, in the following order from bottom to top:

    [condition, else-branch, then-branch]
    
So, the `if` word will execute the `then-branch` of code if `condition` is equal
to `true`, otherwise it will execute the `else-branch`.  Note, *both* branches
are required, and both branches need to be executable, so they should be anonymous
macros.  If you don't need an `else-branch` for your purpose, you should provide
`#(nop)` as the `else-branch`, meaning "do nothing.".

Here's an example:

    3 1 > #("3 < 1!" . pop) #("3 > 1!" . pop) if
    
This will print "3 > 1!", as `3 1 >` evaluated to `true`, so the `then-branch`
was executed.

**Recursion**
Recursion is done by writing macros which reference themselves, in the non-base 
case of some if-branch.  There are no builtins for looping constructs, so recursion
is the order of the day for looping.  Note: the standard library provides the `ntimes`
macro, which will perform an action a specified number of times.

**Script file support**
You can run a file of valid pila words by placing it's relative path as a string
on the stack, and then running the `!import` word.

**Comments**
Comments are delimited by `//`.  Anything to the right of `//` to the end of a line
is discarded and ignored by the parser.  This works both in the REPL and in script
files.  You can use this as a way to add documentation to macros that will be 
visible in the repl when using the `!macros` word, just be sure to keep your comments
on the same line when doing this!

**Strings**
Some escape character sequences are recognized in pilaje:

Character Sequence | Meaning
-------------------|--------
\t                 | Insert a tab character.
\r                 | Insert a carrage return character.
\f                 | Insert a linefeed character.
\n                 | Insert a newline character.
\"                 | Insert a literal double-quote.
\\                 | Insert a literal backslash.

Single quotes (`'`) have no meaning in pilaje, so they do not need to
be escaped.

**Meta-programming**
pila allows some limited meta-programming, via the fact that both macros and anonymous 
macros are allowed to define new macros.  For example:

    [0]> :macro1 #(:macro2 "I'm macro2, and I didn't exist when macro1 was called!" . pop) call
    [0]> macro2
      >> ERROR: Unknown word, ignoring: macro2
    [0]> macro1
    [0]> macro2
    "I'm macro2, and I didn't exist when macro1 was called!"
    
You may be able to use this cleverly.

