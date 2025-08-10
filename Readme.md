Alright — here’s your **updated full README** for the **📊 Smart Daily Expense Tracker** module, incorporating the **date-picker-aware "Total Spent" header** logic and other module details in a clean, dev-friendly way.

---

# 📊 Smart Daily Expense Tracker

**AI-First Assignment for Small Business Owners**

---

## 📌 Overview

The **Smart Daily Expense Tracker** is a **full-featured Jetpack Compose module** that empowers **small business owners** to easily record, analyze, and export daily expenses.
It’s designed with an **AI-first approach** — meaning **minimal manual entry** and **automatic intelligent insights** into cash flow patterns.

With this tool, you’ll never lose track of an expense — whether it was in a WhatsApp chat, a paper slip, or just in memory.

---

## ✨ Features

### **1. Date Picker Integration**

* **Dynamic Date Selection**: Choose any date using a date picker.
* **Automatic Filtering**: Expenses list is automatically filtered by the selected date.
* **Smart Header Text**:

  * Shows **"Total Spent Today"** if the selected date is today.
  * Shows **"Total Spent on \[Date]"** if it’s any other date (e.g., `Total Spent on 10 August 2025`).

---

### **2. Grouped Expense Display**

* **Flexible Grouping**:

  * Group expenses **by category** (e.g., Food, Utilities, Office Supplies).
  * Group expenses **by time** (e.g., Morning, Afternoon, Evening).
* **Auto-Calculated Group Totals**:

  * Each group header displays its summed total.
* **Clean UI in LazyColumn**:

  * Group headers (CategoryHeader or TimeHeader).
  * Expense items nested under each group.

---

## 🧠 AI Usage

The AI-first design helps in:

* **Auto-categorizing expenses** from textual descriptions.
* **Pattern detection** for recurring expenses.
* **Predictive budget alerts** when approaching daily or monthly spending limits.

---

## 🛠 Implementation Details

### **Updated Header Composable**

The header text changes dynamically depending on whether the selected date is today:

```kotlin
@Composable
fun ExpenseHeader(
    totalSpentToday: Double,
    selectedDate: LocalDate
) {
    val formatter = remember { DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault()) }
    val today = remember { LocalDate.now() }

    val dateText = if (selectedDate == today) {
        stringResource(R.string.total_spent_today)
    } else {
        "Total Spent on ${selectedDate.format(formatter)}"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = dateText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "₹${String.format(Locale.US, "%.2f", totalSpentToday)}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
```

---

### **Usage in Screen**

```kotlin
ExpenseHeader(
    totalSpentToday = totalSpentToday.doubleValue,
    selectedDate = viewModel.selectedDate.value
)
```

* `selectedDate` comes from the ViewModel’s `onDateSelected(localDate)` method.
* `totalSpentToday` is the calculated sum for that date’s expenses.

---

### **ViewModel Date Handling**

```kotlin
fun onDateSelected(localDate: LocalDate) {
    _selectedDate.value = localDate
    filterExpensesByDate(localDate)
}
```

---

## 📂 Module Structure

```
smart-daily-expense-tracker/
│── ui/
│   ├── ExpenseHeader.kt      # Header with dynamic date logic
│   ├── ExpenseList.kt        # LazyColumn with grouping support
│   ├── GroupHeaders.kt       # CategoryHeader & TimeHeader
│── viewmodel/
│   ├── ExpenseViewModel.kt   # Date selection, filtering, and total calculation
│── model/
│   ├── Expense.kt            # Expense data model
│── resources/
│   ├── strings.xml           # UI text resources
│   ├── themes.xml
```

---

## 📦 Future Enhancements

* OCR-based expense entry from receipts.
* AI-powered recurring expense detection.
* Multi-currency support.
* Export to Excel / PDF.
* Cloud sync with Google Drive or Dropbox.

---

## 📜 License

MIT License — Free for personal and commercial use.

---

This version of the README not only explains **how the date header works dynamically** but also gives **full module context** so a dev or business owner can understand the entire setup in one place.

---

If you want, I can also **add a visual flow diagram** to show how the date picker updates the header and the filtered expenses list. That would make the README even more engaging.
