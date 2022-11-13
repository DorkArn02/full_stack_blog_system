package hu.pte.blog_backend.services;

import hu.pte.blog_backend.models.Comment;
import hu.pte.blog_backend.models.User;
import hu.pte.blog_backend.models.dtos.CommentRequestDTO;
import hu.pte.blog_backend.models.dtos.CommentResponseDTO;
import hu.pte.blog_backend.repository.CommentRepository;
import hu.pte.blog_backend.repository.PostRepository;
import hu.pte.blog_backend.repository.UserRepository;
import hu.pte.blog_backend.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final JwtUtil jwtUtil;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository, JwtUtil jwtUtil) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Get comment by id
     * @param id Comment's id
     * @return CommentResponseDTO
     */
    public CommentResponseDTO getCommentById(UUID id){
        if(commentRepository.findById(id).isPresent()){
            Comment comment = commentRepository.findById(id).get();
            return new CommentResponseDTO(comment, userRepository.findById(comment.getUser_id().getUser_id()).get().getUsername());
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Add comment to post
     * @param post_id Post's id
     * @param comment CommentRequestDTO
     * @param token Bearer authorization token
     * @return CommentRequestDTO
     */
    public CommentRequestDTO addCommentToPost(UUID post_id, CommentRequestDTO comment, String token)
    {
        if(userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).isPresent()){
            if(postRepository.findById(post_id).isPresent()){
                Comment com = new Comment();
                com.setUser_id(userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).get());
                com.setPost_id(postRepository.findById(post_id).get());
                com.setCreatedAt(LocalDateTime.now());
                com.setUpdatedAt(LocalDateTime.now());
                com.setContent(comment.getContent());

                return new CommentRequestDTO(commentRepository.save(com));
            }else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete comment by id
     * @param comment_id Comment's id
     * @param token Bearer authorization token
     */
    public void deleteCommendById(UUID comment_id, String token){
        if(commentRepository.findById(comment_id).isPresent()){
            if(userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).isPresent()){

                User user = userRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).get();
                Comment comment = commentRepository.findById(comment_id).get();

                if(user.getUser_id().equals(comment.getUser_id().getUser_id())){
                    commentRepository.deleteById(comment_id);
                }else{
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                }

            }else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
