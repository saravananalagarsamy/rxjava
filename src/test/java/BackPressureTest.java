import io.reactivex.*;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import org.testng.annotations.Test;

/**
 * Created by m509575 on 24/08/17.
 */
public class BackPressureTest {

    Observable<Long> pressureObservable = Observable.create(new ObservableOnSubscribe<Long>() {
        long count;

        @Override
        public void subscribe(ObservableEmitter<Long> e) throws Exception {
            while (true) {
                e.onNext(count);
                count++;
            }
        }
    });


    Flowable<Long> flowableObservable = Flowable.create(new FlowableOnSubscribe<Long>() {
        long count;

        @Override
        public void subscribe(FlowableEmitter<Long> e) throws Exception {
            while (true) {
                e.onNext(count);
                count++;
            }
        }
    }, BackpressureStrategy.ERROR);

    @Test
    public void testBackPressure() throws Exception {
        pressureObservable.observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   Thread.sleep(5);
                                   System.out.println("Received " + aLong);
                               }
                           }
                );

    }

    @Test
    public void testFlowableBackPressureAsAnError() throws Exception {
        flowableObservable.observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Thread.sleep(5);
                        System.out.println("Received " + aLong);
                    }
                });

    }


    @Test
    public void testMeterialize(){
        Observable<Integer> observableInt = Observable.<Integer>create(s->{
            s.onNext(50);
            s.onNext(150);
            s.onComplete();
        });

        observableInt.materialize().filter((t-> t.isOnNext())).dematerialize().subscribe(t -> System.out.println(t));

        observableInt.materialize().filter(integerNotification -> integerNotification.isOnNext() || integerNotification.isOnComplete()).dematerialize().subscribe(System.out::println);
    }


}
