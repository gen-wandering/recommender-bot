package com.recommender.bot.service.data;

import com.recommender.bot.entities.InnerId;
import com.recommender.bot.repository.InnerIdRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InnerIdServiceImpl implements InnerIdService {
    private final InnerIdRepository innerIdRepository;

    public InnerIdServiceImpl(InnerIdRepository innerIdRepository) {
        this.innerIdRepository = innerIdRepository;
        init();
    }

    private final int cacheSize = 5;
    private final List<Integer> cachedIds = new ArrayList<>(cacheSize);

    private void init() {
        InnerId sequence = innerIdRepository.findAll().get(0);

        cachedIds.clear();

        int start = sequence.getValue();
        for (int i = start; i < start + cacheSize; i++) {
            cachedIds.add(i);
        }
        sequence.setValue(start + cacheSize);
        innerIdRepository.save(sequence);
    }

    @Override
    public int next() {
        for (int i = 0; i < cacheSize; i++) {
            if (cachedIds.get(i) != -1)
                return cachedIds.set(i, -1);
        }
        init();
        return cachedIds.set(0, -1);
    }
}