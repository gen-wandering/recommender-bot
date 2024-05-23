package com.recommender.bot.service.data;

import com.recommender.bot.entities.Viewer;
import com.recommender.bot.repository.ViewerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewerServiceImpl implements ViewerService {
    private final ViewerRepository viewerRepository;

    public ViewerServiceImpl(ViewerRepository viewerRepository) {
        this.viewerRepository = viewerRepository;
    }

    @Override
    public int saveViewer(Viewer viewer) {
        if (viewerRepository.existsById(viewer.getId()))
            return -1;
        else
            return viewerRepository.save(viewer).getId();
    }

    @Override
    public Viewer getViewer(int id) {
        return viewerRepository.getViewerWithRatings(id);
    }

    @Override
    public void deleteViewer(int viewerId) {
        viewerRepository.deleteById(viewerId);
    }

    @Override
    public List<Integer> getAllViewerIds() {
        return viewerRepository.findAll().stream().map(Viewer::getId).toList();
    }

    @Override
    public List<Viewer> getAllViewersWithRatings() {
        return viewerRepository.getAllViewersWithRatings();
    }

    @Override
    public Integer getAmountOfViewers() {
        return viewerRepository.getAmountOfViewers();
    }
}
