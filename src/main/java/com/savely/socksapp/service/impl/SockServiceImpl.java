package com.savely.socksapp.service.impl;

import com.savely.socksapp.dto.SockDto;
import com.savely.socksapp.entity.Sock;
import com.savely.socksapp.exception.IncorrectOperationException;
import com.savely.socksapp.exception.IncorrectPaginationException;
import com.savely.socksapp.exception.IncorrectQuantityException;
import com.savely.socksapp.exception.SockNotFoundException;
import com.savely.socksapp.mapper.SockMapper;
import com.savely.socksapp.repository.SockRepository;
import com.savely.socksapp.service.SockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SockServiceImpl implements SockService {

    private final SockRepository sockRepository;
    private final SockMapper sockMapper;

    @Transactional
    @Override
    public void income(SockDto sockDto) {
        sockRepository.findByColorAndCottonPart(sockDto.getColor(), sockDto.getCottonPart()).ifPresentOrElse(
                sock -> {
                    sock.setQuantity(sock.getQuantity() + sockDto.getQuantity());
                    sockRepository.save(sock);
                }, () -> {
                    sockDto.setId(0L);
                    sockRepository.save(sockMapper.toEntity(sockDto));
                }
        );
    }

    @Transactional
    @Override
    public void outcome(SockDto sockDto) {
        Sock sock = sockRepository.findByColorAndCottonPart(sockDto.getColor(), sockDto.getCottonPart())
                .orElseThrow(SockNotFoundException::new);
        if (sock.getQuantity() < sockDto.getQuantity()) {
            throw new IncorrectQuantityException();
        } else if (sock.getQuantity() == sockDto.getQuantity()) {
            sockRepository.deleteById(sock.getId());
        } else {
            sock.setQuantity(sock.getQuantity() - sockDto.getQuantity());
            sockRepository.save(sock);
        }
    }


    @Override
    public List<SockDto> getSocks(String color, String operation, int cottonPart) {
        List<Sock> socks =
                switch (operation) {
                    case "lessThan" -> sockRepository.findByColorAndCottonPartLessThan(color, cottonPart);
                    case "moreThan" -> sockRepository.findByColorAndCottonPartGreaterThan(color, cottonPart);
                    default -> throw new IncorrectOperationException();
                };
        return socks.stream()
                .map(sockMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SockDto> getSocks(int page, int size) {
        if (page <= 0 || size <= 0) {
            throw new IncorrectPaginationException();
        }
        return sockRepository.findAll(PageRequest.of(page - 1, size))
                .stream()
                .map(sockMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SockDto getSockEqual(String color, int cottonPart) {
        return sockRepository.findByColorAndCottonPart(color, cottonPart)
                .map(sockMapper::toDto)
                .orElseThrow(SockNotFoundException::new);
    }
}
