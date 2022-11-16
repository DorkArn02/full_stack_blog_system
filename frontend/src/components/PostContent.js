import { Alert, Avatar, Box, Button, Divider, Flex, Heading, HStack, Image, Input, InputGroup, Spacer, Text, VStack } from '@chakra-ui/react'
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import postServices from '../services/post-services'
import moment from 'moment'
import parse from "html-react-parser"
import authServices from '../services/auth-services'
import { FaPencilAlt, FaTrash } from 'react-icons/fa'
import commentService from '../services/comment-service'
import { CommentBox } from './CommentBox'

export const PostContent = () => {

    const [data, setData] = useState()


    const [comment, setComment] = useState()

    const { id } = useParams()

    useEffect(() => {
        postServices.getPostById(id).then(resp => setData(resp))
    }, [id])

    const navigate = useNavigate()

    if (data == null) {
        return ""
    }

    const sendComment = (e) => {
        e.preventDefault()

        if (comment == null || comment == "") {
            return;
        }
        commentService.addNewComment(id, comment).then(resp => {
            console.log(resp)
        }).catch(err => {
            console.log(err)
        })

    }
    return (
        <>
            <VStack justify={"center"} mb={10}>
                <Image maxWidth={"50%"} src={`http://localhost:8080/api/posts/picture/${id}`} />
                {authServices.getCurrentUser() != null &&
                    authServices.getCurrentUser().user_id === data.user_id ?
                    <HStack w={"50%"}>
                        <Button leftIcon={<FaTrash />} colorScheme={"pink"} onClick={() => { postServices.deletePostById(id); navigate('/') }}>Delete</Button>
                        <Button leftIcon={<FaPencilAlt />} onClick={() => navigate(`/edit/${data.post_id}`)}>Edit</Button>
                        <Spacer />
                        <Text>Posted by: {data.author} - {moment().to(data.createdAt)}</Text>
                    </HStack>
                    : ""}
                <Divider width={"50%"} />
            </VStack>
            <Flex justify={"center"}>
                <Flex gap={0} width={"50%"} p={4} flexDir={"column"} boxShadow={"xl"}>
                    <Heading mb={4} size={"lg"} textAlign={"center"}>{data.title} </Heading>
                    <Text textAlign={"justify"}>{parse(data.content)}</Text>
                    <Divider />
                    <Flex mt={10} flexDir={"column"}>
                        <Heading mb={2} size="lg">Comments:</Heading>
                        {data.comments.length != 0 ? data.comments.map(item => {
                            return <CommentBox data={item} />
                        }) : "No available comments for this post!"}
                        {authServices.getCurrentUser() === null ? <Alert variant={"left-accent"}>"Please login to write comments."</Alert> :
                            <Box mt={20} p={2} border="1px dotted white">
                                <form onSubmit={sendComment}>
                                    <InputGroup mb={2}>
                                        <Input onChange={(e) => setComment(e.target.value)} type="text" name="content" placeholder='Write here your text...' required />
                                    </InputGroup>
                                    <Button type="submit">Send comment</Button>
                                </form>
                            </Box>}
                    </Flex>
                </Flex>
            </Flex>

        </>
    )
}
