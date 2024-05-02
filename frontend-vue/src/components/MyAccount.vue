<template>
  <div class="account">
    <h2>Current Account</h2>
    <div class="account-details">
      <div class="account-info">
        <div>D.A KAHYA</div>
        <div>IBAN: NLXX BANK XXXX XXXX XXXX XX</div>
      </div>
      <div class="balance">{{ '€' + saldo }}</div>
    </div>
    <button class="transfer-button" @click="openTransferModal">Transfer</button>

    <div class="transaction-container">
      <h3>Transactions</h3>
      <div class="transaction-list">
        <ul>
          <li v-for="transaction in transactions" :key="transaction.id" class="transaction">
            <div>
              <span class="transaction-date">{{ transaction.date }}</span>
              <span>{{ transaction.name }}</span>
            </div>
            <span :class="{ 'credit': transaction.amount >= 0, 'debit': transaction.amount < 0 }">
              {{ transaction.amount >= 0 ? '+' : '' }}{{ transaction.amount }}
            </span>
          </li>
        </ul>
      </div>
      <div v-if="transactions.length === 0" class="no-transactions">
        No transactions available.
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// Simulated data for saldo and transactions
const saldo = ref(1000);
const transactions = ref([
  { id: 1, name: 'Salary', amount: 200, date: '2024-05-01' },
  { id: 2, name: 'Groceries', amount: -50, date: '2024-05-02' },
  { id: 3, name: 'Online Purchase', amount: -100, date: '2024-05-02' },
  { id: 4, name: 'Groceries', amount: -300, date: '2024-05-03'}
]);

const openTransferModal = () => {
  // Implement transfer modal logic here
};
</script>

<style scoped>
.account {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

h2 {
  margin-top: 0;
}

.account-details {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.account-info {
  flex: 1;
}

.balance {
  font-weight: bold;
  font-size: 25px;
}

.transfer-button {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-bottom: 20px;
}

.transaction-container {
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 20px;
}

.transaction-list {
  margin-top: 20px;
}

.transaction-date {
  font-size: 12px;
  color: #888;
  margin-right: 30px;
}

.transaction {
  display: flex;
  justify-content: space-between;
  align-items: center; /* Verticaal centreren */
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ccc;
}

.credit {
  color: green;
}

.debit {
  color: red;
}

.no-transactions {
  padding: 10px 0;
}
</style>
