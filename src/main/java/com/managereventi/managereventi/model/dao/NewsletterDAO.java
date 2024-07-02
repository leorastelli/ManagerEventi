package com.managereventi.managereventi.model.dao;

import java.util.List;
import com.managereventi.managereventi.model.mo.Newsletter;

public interface NewsletterDAO {
    public Newsletter subscribeToNewsletter(String idUtente, String email);
    public void unsubscribeFromNewsletter(String idUtente, String email);
    public boolean isSubscribed(String idUtente, String email);
    public List<String> getMailList(String idUtente);
}

