/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.*;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.GroupedObservable;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import rx.Scheduler;
//import io.reactivex.schedulers;
import rx.schedulers.Schedulers;

/**
 *
 * @author m509575
 */
public class ObservableTest {
    
    public ObservableTest() {
    }
    
     @Test //consumer 
     public void testObservable() {
         Observable.<Integer>create(s -> {
             s.onNext(150);
             s.onNext(100);
             s.onComplete();
             }).subscribe(new Consumer<Integer>(){
				@Override
				public void accept(Integer arg0) throws Exception {
					System.out.println(arg0);
				}
            	 
             });
    }

     
     @Test //consumer alternative
     public void testObservable2() {
         Observable.<Integer>create(s -> {
             s.onNext(150);
             s.onNext(100);
             s.onComplete();
             }).subscribe(arg0 -> System.out.println(arg0)); // Lambda expression 
    }


     @Test //consumer alternative
     public void testObservable3() {
         Observable.<Integer>create(s -> {
             s.onNext(150);
             s.onNext(100);
             s.onComplete();
             }).subscribe(System.out::println); // method reference 
    }
     
     @Test //consumer alternative with on error
     public void testObservable4() {
         Observable.<Integer>create(s -> {
             s.onNext(150);
             throw new Exception("omg error");
             //s.onNext(100);
             //s.onComplete();
             }).subscribe(System.out::println,e -> System.out.println(e.getMessage())); // method reference 
    }
     
     
     @Test //consumer alternative with multiple subscriber
     public void testObservable5() {
         Observable<Integer> observableSource = Observable.<Integer>create(s -> {
             s.onNext(150);
             s.onNext(100);
             s.onComplete();
             });
         
		observableSource.subscribe(arg0 -> {
			System.out.println("Subs 1 On Next" + arg0);
			System.out.println("Subs 1"+Thread.currentThread().getName());
		}); // method reference 
        
		observableSource.subscribe(arg0 -> {
			System.out.println("Subs 2 On Next" + arg0);
			System.out.println("Subs 2"+Thread.currentThread().getName());
		}); // method reference 

     }
     
     @Test //consumer alternative with multiple subscriber with actions and map and repeat
     public void testObservable6() {
         Observable<Integer> observableSource = Observable.<Integer>create(s -> {
             s.onNext(150);
             s.onNext(100);
             s.onComplete();
             });
         
		observableSource.map(x -> x+10).subscribe(arg0 -> {
			System.out.println("Subs 1 On Next" + arg0);
			System.out.println("Subs 1"+Thread.currentThread().getName());
		}); // method reference 
        
		observableSource.repeat(5).subscribe(arg0 -> {
			System.out.println("Subs 2 On Next" + arg0);
			System.out.println("Subs 2"+Thread.currentThread().getName());
		}); // method reference 

     }

     @Test
     public void simplifiedObservable(){
        Observable.just(50,150).subscribe(System.out::println);
     }
     
     @Test
     public void withInterval() throws InterruptedException{
        Observable.interval(1,TimeUnit.SECONDS).subscribe(System.out::println);
        Thread.sleep(6000);
     }

    @Test
    public void withIntervalScheduler() throws InterruptedException{
        Observable.interval(1,TimeUnit.SECONDS).subscribe(System.out::println);
        Thread.sleep(6000);
    }

    @Test
    public void withRange(){
        Observable.range(2,30).subscribe(System.out::println);

    }

    @Test
    public void RangewithFilter() {
        Observable.range(1,50).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer % 2 != 0;
            }
        }).subscribe(System.out::println);
    }

    @Test
    public void testFuture(){
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Integer> future = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println(Thread.currentThread().getName());
                Thread.currentThread().sleep(1000);
                return 10 + 50;
            }
        });

        Observable.fromFuture(future).doOnNext(x -> printLog(x + Thread.currentThread().getName())).subscribe(t -> System.out.println(Thread.currentThread().getName()+" "+t));

    }

    @Test
    public void testDefer() throws InterruptedException{
        Observable<LocalTime> deferObservable = Observable.defer(() -> Observable.just(LocalTime.now()));
        deferObservable.subscribe(t -> System.out.println(t));
        Thread.sleep(3000);
        deferObservable.subscribe(t -> System.out.println(t));

        Observable<Integer> deferObservableInt = Observable.defer(() -> Observable.range(1,5));
        deferObservableInt.subscribe(t -> System.out.println(t));
    }

    @Test
    public void testFlatMap(){
        Observable<Integer> observable = Observable.range(1,3);
        Observable<Observable<Integer>> observablemap = observable.map(x -> Observable.just(x+1,x+2,x+3));

        observable.subscribe(x -> System.out.println(x));
        observablemap.subscribe(x -> System.out.println(x));

        Observable<Integer> observableFlatmap = observable.flatMap(x -> Observable.just(x+1,x+2,x+3));
        observableFlatmap.subscribe(x -> System.out.println(x));
    }


    public Observable<String> getStockHistoricalPrices(String stockName) throws IOException {
        URL url = new URL("https://www.google.com/finance/historical?q=" + stockName + "&output=csv");
        InputStream ip = url.openConnection().getInputStream();
        InputStreamReader isr = new InputStreamReader(ip);
        BufferedReader br = new BufferedReader(isr);

        return Observable.create(e -> {
            br.lines().forEach(e::onNext);
            System.out.println("----------------Making web call--------------------");
            e.onComplete();
                }
        );
    }

    @Test
    public void testRealLifeFlatMap() throws IOException, InterruptedException {

        Observable<String> stocksObservable = getStockHistoricalPrices("GOOG");
        stocksObservable.subscribe(System.out::println);

        Observable<String> observableStocks = Observable.just("GOOD","M","MAC","ORCL","AAPL");
        Observable<Observable<String>> mapofStocks = observableStocks.map(ss -> getStockHistoricalPrices(ss));
        mapofStocks.subscribe(t -> System.out.println(t));

        Observable<String> stringObservable = observableStocks.flatMap((ss -> getStockHistoricalPrices(ss)));
        stringObservable.subscribe(t -> System.out.println(t),throwable -> throwable.printStackTrace());

        Observable<String> stringObservable1 = observableStocks.flatMap((ss -> getStockHistoricalPrices(ss).delay(2, TimeUnit.SECONDS)));
        stringObservable1.subscribe(t -> System.out.println(t),throwable -> throwable.printStackTrace(),() -> System.out.println("Done"));
        Thread.sleep(2000);

    }

    @Test
    public void testRealTimeZip(){
        Observable<String> observableStocks = Observable.just("GOOG","M","MAC","ORCL","AAPL");

        Observable<String> rawData = observableStocks.flatMap(s -> getStockHistoricalPrices(s).skip(1).take(1));

        Observable<Tuple<String, String>> tupleObservable = observableStocks.zipWith(rawData, (s, s2) -> new Tuple<>(s, s2));

        tupleObservable.subscribe(s -> {
            System.out.println(s.getU() + " " + s.getV());
        });

    }


    @Test
    public void testRealTimeZipWithTouple(){
        Observable<String> observableStocks = Observable.just("GOOG","M","MAC","ORCL","AAPL");

        Observable<String> rawData = observableStocks.flatMap(s -> getStockHistoricalPrices(s).skip(1).take(1));

        Observable<Stocks<Object, Object>> stocksObservable = observableStocks.zipWith(rawData, (s, s2) -> new Stocks<>(s, s2));

        stocksObservable.subscribe(s -> {
            System.out.println(s.getStockName() + " => Open = " + s.getStockData().getOpenData());
        });

    }

    @Test
    public void testRealTimeZipWithToupleCache(){
        Observable<String> observableStocks = Observable.just("GOOG","M","MAC","ORCL","AAPL");

        Observable<String> rawData = observableStocks.flatMap(s -> getStockHistoricalPrices(s).skip(1).take(1).cache());

        Observable<Stocks<Object, Object>> stocksObservable = observableStocks.zipWith(rawData, (s, s2) -> new Stocks<>(s, s2));

        stocksObservable.subscribe(s -> {
            System.out.println(s.getStockName()  +" => Open = " + s.getStockData().getOpenData());
        });

        // From this subscribe observable will serve data from cache
        stocksObservable.subscribe(s -> {
            System.out.println(s.getStockName() + " => Open = " + s.getStockData().getOpenData());
        });

        stocksObservable.subscribe(s -> {
            System.out.println(s.getStockName() + " => Open = " + s.getStockData().getOpenData());
        });
    }

    @Test
    public void testGroupBy(){
        Observable<String> observableStocks = Observable.just("GOOG","M","MAC","ORCL","AAPL");

        Observable<String> rawData = observableStocks.flatMap(s -> getStockHistoricalPrices(s).skip(1));

        Observable<Tuple<String, String>> tupleObservable = observableStocks.zipWith(rawData, (s, s2) -> new Tuple<>(s, s2));

        tupleObservable.subscribe(t -> System.out.println(t.getU()));

        Observable<GroupedObservable<String, String>> groupedObservableObservable = tupleObservable.groupBy(t -> t.getV(), t -> t.getU());

        groupedObservableObservable.subscribe(t -> System.out.println(t.getKey()));

    }





    public void printLog(String message){
        System.out.println(message);
    }


    @Test //with observer
     public void testObservabl2e() {
         Observable.<Integer>create(s -> {
             s.onNext(150);
             s.onNext(100);
             s.tryOnError(null);
             s.onComplete();
             }).subscribe(new Observer<Integer>(){

				@Override
				public void onComplete() {
					System.out.println("printing on complete");
					
				}

				@Override
				public void onError(Throwable arg0) {
					System.out.println("printing error"+arg0);
					
				}

				@Override
				public void onNext(Integer arg0) {
					try {
						throw new Exception("hello");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("printing number"+arg0);
					
				}

				@Override
				public void onSubscribe(Disposable arg0) {
					System.out.println("printing on subscribe"+arg0);
					
				}
				
            	 
             });
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }
}
