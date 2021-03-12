package assignment.filedownloader.service.mapper;

import assignment.filedownloader.domain.IProtocol;
import assignment.filedownloader.domain.Protocol;
import assignment.filedownloader.service.downloader.FTPDownloaderService;
import assignment.filedownloader.service.downloader.HTTPDownloaderService;
import assignment.filedownloader.service.downloader.SFTPDownloaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Optional;

/*
 * This service is using for returning downloading service regarding incoming protocol
 */
@Service
public class ProtocolMapperService {

    private final EnumMap<Protocol, IProtocol> protocolMapper;

    @Autowired
    public ProtocolMapperService(HTTPDownloaderService httpDownloaderService, FTPDownloaderService ftpDownloaderService, SFTPDownloaderService sftpDownloaderService) {
        this.protocolMapper = new EnumMap<>(Protocol.class);
        protocolMapper.put(Protocol.HTTP, httpDownloaderService);
        protocolMapper.put(Protocol.FTP, ftpDownloaderService);
        protocolMapper.put(Protocol.SFTP, sftpDownloaderService);
    }

    public IProtocol getProtocol(Protocol protocol) {
        return Optional.ofNullable(protocolMapper.get(protocol)).orElseThrow(() -> new IllegalArgumentException("Unsupported protocol"));
    }
}
