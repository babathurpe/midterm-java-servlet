/*
 * Copyright 2015 Len Payne <len.payne@lambtoncollege.ca>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Account;

/**
 * Provides an Account Balance and Basic Withdrawal/Deposit Operations
 */
@WebServlet("/account")
public class AccountServlet extends HttpServlet {

    Account account = new Account();
    double cash = 0.0;
    double balance = 0.0;

    private double refreshBalance() {
        balance = account.getBalance();
        return balance;
    }

    private double withdraw() {
        balance = account.withdraw(cash);
        return balance;
    }

    private double deposit() {
        balance = account.deposit(cash);
        return balance;
    }

    private void closeAccount() {
        account.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        try (PrintWriter out = response.getWriter()) {
            out.println(refreshBalance());
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Set<String> keySet = request.getParameterMap().keySet();
        try (PrintWriter out = response.getWriter()) {
            
            //when the deposit button is clicked
            if (keySet.contains("deposit")) {
                String cashDeposit = request.getParameter("deposit");
                cash = Double.parseDouble(cashDeposit);
                deposit();
                out.print(refreshBalance());
            }
            //when the close account button is clicked
            if (keySet.contains("close")) {
                closeAccount();
            }
            //when the withdraw button is clicked
            if (keySet.contains("withdraw")) {
                String cashDeposit = request.getParameter("withdraw");
                cash = Double.parseDouble(cashDeposit);
                withdraw();
                out.print(refreshBalance());
            }
        }
    }
}
