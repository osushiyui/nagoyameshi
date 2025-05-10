const stripe = Stripe('pk_test_51QuYTm4CcUmRLRc6SAMz5cQwDkRyIK8Yrma5JNPAaIuHvvJUqL595obZV0ydubW7W3p5TE0ObFRQrEj9h00CsnlV00XwZvbbBV');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
 stripe.redirectToCheckout({
   sessionId: sessionId
 })
});