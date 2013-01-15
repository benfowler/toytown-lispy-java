(begin
  (define seq (lambda (lbound ubound)
    (if (> lbound ubound)
      (quote ())
      (cons lbound (seq (+ lbound 1) ubound)))))
              
  (define filter (lambda (predicate argument)
    (if (null? argument)
      (quote ())
      (if (predicate (car argument))
        (cons (car argument) (filter predicate (cdr argument)))
        (filter predicate (cdr argument))))))

  (define notDivisibleBy (lambda (n)
    (lambda (x)
      (> (% x n) 0))))

  (define firstGreaterThan (lambda (n argument)
    (if (null? argument)
      (quote ())
      (if (> (car argument) n)
        (car argument)
        (firstGreaterThan n (cdr argument))))))
        
  (define sieve (lambda (p lst ubound) 
    (begin
      (define newLst (filter (notDivisibleBy p) lst))
      (define newP (firstGreaterThan p newLst))
      (if (null? newP)
        lst
        (cons p (sieve newP newLst ubound))))))

  (define primes (lambda (ubound)
    (sieve 2 (seq 2 ubound) ubound)))

  (primes 200)
)
