package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Optional<Reservation> optionalReservation = reservationRepository2.findById(reservationId);
        Payment payment=new Payment();

        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            // this reservation has the detalis of how many hours this reservation of spot  made by the user is given
//             payment = reservation.getPayment();

//            if (payment.getPaymentCompleted()) return payment;

            int hours = reservation.getNumberOfHours();
            Spot spot = reservation.getSpot();
            int costPerHour = spot.getPricePerHour();
            int totalAmount = hours * costPerHour;
            if (amountSent >= totalAmount) {
                payment.setPaymentCompleted(true);
            } else {
//            throw new Exception("Insufficient Amount");
                return payment;
            }
            if (mode != null) {
                mode = mode.toUpperCase();

                if (mode.equals(PaymentMode.CARD.name())) payment.setPaymentMode(PaymentMode.CARD);
                else if (mode.equals(PaymentMode.UPI.name())) payment.setPaymentMode(PaymentMode.UPI);
                else if (mode.equals(PaymentMode.CASH.name())) payment.setPaymentMode(PaymentMode.CASH);
//        else throw new Exception("Payment mode not detected");
                else {
                    return payment;
                }
            }

            payment.setReservation(reservation);
            reservation.setPayment(payment);
            reservationRepository2.save(reservation);
        }
        return payment;

    }
}
