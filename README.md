Overview
========

[toytown-lispy-java][1] is a Java re(re-re-re)implementation of Peter Norvig's 
famous hack demonstrating how to implement Lisp in less than 100 lines of 
Python. 

Unlike Python, Java's language features do not give a lot away for free, so
the author had a lot more work to do, and the resulting code weighed in at 
_slightly_ more than 100 lines of Java. 

This isn't really a problem, because the code is still quite compact and 
readable, and the built JAR weighs in at a mere 18 kilobytes in size.  

Norvig actually wrote _two_ versions of Lispy: [the first][2], which was his
first attempt to write a Lisp interpreter in less than 100 lines of code; this
lacks error checking and a lot of important features, but is surprisingly
powerful, given its size.  His [second attempt][3] is a more complete version,
which has better error checking, a more complete set of builtins, hygienic
macros, tail call optimisation, additional data types, call/cc and a few other
refinements.

Toytown implements a modest superset of the former, which includes Boolean
types and (with big limitations) Java interoperability features lifted from 
Clojure.  It also comes with Peter Norvig's test suite, which it passes.


Support
=======

Toytown is supplied as-is.  If it breaks, you get to keep both pieces.


Example Usage
=============

A few examples are below, but you my also find example usage in the 
`/src/test` directory.


Hello World
-----------

(Whoops.  Toytown doesn't yet support string literals, so this will have to
wait. Sorry!!)


Fibonacci Sequences
-------------------

    (begin 
    
      (define fact (lambda (n) 
        (if (<= n 1) 
          1 
          (* n (fact (- n 1))))))
          
      (fact 13))


Closures
--------

    (begin
                  
      (define filter (lambda (predicate argument)
        (if (null? argument)
          (quote ())
          (if (predicate (car argument))
            (cons (car argument) (filter predicate (cdr argument)))
            (filter predicate (cdr argument))))))

      (define notDivisibleBy (lambda (n)
        (lambda (x)
          (> (% x n) 0))))

      (filter (notDivisibleBy 2) (list 1 2 3 4 5 6 7 8)))
      
      
Java Interop
------------

Some examples.  Toytown's support for Java interoperability was only added to
easily allow implementing all the language builtins in Java, and has a long
way to go before it's truly useful.


    (.nanoTime java.util.System)
    
    (.toHexString 65534)

    (begin
        (define fred 65534)
        (.isInfinite fred))


[1]: https://github.com/benfowler/toytown-lispy-java
[2]: http://norvig.com/lispy.html
[3]: http://norvig.com/lispy2.html
