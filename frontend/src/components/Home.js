import { Divider, Grid, Heading, Skeleton, VStack } from '@chakra-ui/react'
import React, { useEffect, useState } from 'react'
import PostServices from '../services/post-services'
import { PostCard } from './PostCard'

export const Home = () => {

    const [post, setPost] = useState()

    useEffect(() => {
        PostServices.getAllPosts().then(resp => setPost(resp))
    }, [])


    return (
        <>
            <VStack justify={"center"} mb={10}>
                <Heading size={"lg"} textAlign={"center"}>View all blog posts</Heading>
                <Divider width={"50%"} />
            </VStack>
            <Grid ml={10} mr={10} gap={20} templateColumns='repeat(auto-fill, minmax(25%, 1fr))'>
                {post ? post.map(item => {
                    return <PostCard key={item.post_id} content={item} />
                })
                    : <Skeleton />
                }
            </Grid>
        </>
    )
}
